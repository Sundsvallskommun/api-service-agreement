package se.sundsvall.agreement.service;

import generated.se.sundsvall.datawarehousereader.PagingAndSortingMetaData;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.agreement.api.model.Agreement;
import se.sundsvall.agreement.api.model.AgreementParameters;
import se.sundsvall.agreement.api.model.AgreementParty;
import se.sundsvall.agreement.api.model.AgreementResponse;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.service.mapper.AgreementMapper;
import se.sundsvall.dept44.problem.ThrowableProblem;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static se.sundsvall.agreement.api.model.Category.WASTE_MANAGEMENT;

@ExtendWith(MockitoExtension.class)
class AgreementServiceTest {

	@Mock
	private AgreementPartyProvider agreementPartyProviderMock;

	@Mock
	private generated.se.sundsvall.datawarehousereader.AgreementResponse agreementResponseMock;

	@Mock
	private AgreementParty agreementPartyMock;

	@Mock
	private Agreement agreementMock;

	@InjectMocks
	private AgreementService agreementService;

	@Test
	void getAgreementsByCategoryAndFacilityIdShouldReturnAgreement() {

		// Arrange
		final var municipalityId = "municipalityId";
		final var facilityId = "facilityId";
		final var onlyActive = true;
		final var category = WASTE_MANAGEMENT;

		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(agreementResponseMock)).thenReturn(of(agreementPartyMock));
			when(agreementPartyProviderMock.getAgreementsByCategoryAndFacility(municipalityId, category, facilityId, onlyActive)).thenReturn(agreementResponseMock);

			// Act
			final AgreementResponse response = agreementService.getAgreementsByCategoryAndFacilityId(municipalityId, category, facilityId, onlyActive);

			// Asset
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(agreementResponseMock));
			verify(agreementPartyProviderMock).getAgreementsByCategoryAndFacility(municipalityId, category, facilityId, onlyActive);

			assertThat(response).isNotNull().extracting(AgreementResponse::getAgreementParties).asInstanceOf(LIST).hasSize(1).first().isSameAs(agreementPartyMock);
		}
	}

	@Test
	void getAgreementsByCategoryAndFacilityIdShouldThrow404() {

		// Arrange
		final var municipalityId = "municipalityId";
		final var facilityId = "facilityId";
		final var onlyActive = false;
		final var category = WASTE_MANAGEMENT;

		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(agreementResponseMock)).thenReturn(emptyList());
			when(agreementPartyProviderMock.getAgreementsByCategoryAndFacility(municipalityId, category, facilityId, onlyActive)).thenReturn(agreementResponseMock);

			// Act
			final var exception = assertThrows(ThrowableProblem.class, () -> agreementService.getAgreementsByCategoryAndFacilityId(municipalityId, category, facilityId, onlyActive));

			// Assert
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(agreementResponseMock));
			verify(agreementPartyProviderMock).getAgreementsByCategoryAndFacility(municipalityId, category, facilityId, onlyActive);

			assertThat(exception.getStatus()).isEqualTo(NOT_FOUND);
			assertThat(exception.getMessage()).isEqualTo("Not Found: No matching agreements were found for facility with id 'facilityId' and category 'WASTE_MANAGEMENT'");
			assertThat(exception.getDetail()).isEqualTo("No matching agreements were found for facility with id 'facilityId' and category 'WASTE_MANAGEMENT'");
		}
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesShouldReturnAgreement() {

		// Arrange
		final var municipalityId = "municipalityId";
		final var partyId = "partyId";
		final var filters = new ArrayList<Category>();
		final var onlyActive = true;

		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(agreementResponseMock)).thenReturn(new ArrayList<>(of(agreementPartyMock)));
			when(agreementPartyProviderMock.getAgreementsByPartyIdAndCategories(any(), any(), any(), anyBoolean())).thenReturn(agreementResponseMock);

			// Act
			final var response = agreementService.getAgreementsByPartyIdAndCategories(municipalityId, partyId, filters, onlyActive);

			// Assert
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(agreementResponseMock));
			verify(agreementPartyProviderMock).getAgreementsByPartyIdAndCategories(municipalityId, partyId, filters, onlyActive);

			assertThat(response).isNotNull().extracting(AgreementResponse::getAgreementParties).asInstanceOf(LIST).hasSize(1).first().isSameAs(agreementPartyMock);
		}
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesShouldThrow404() {

		// Arrange
		final var municipalityId = "municipalityId";
		final var partyId = "partyId";
		final var filters = new ArrayList<Category>();
		final var onlyActive = false;

		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(agreementResponseMock)).thenReturn(emptyList());
			when(agreementPartyProviderMock.getAgreementsByPartyIdAndCategories(any(), any(), any(), anyBoolean())).thenReturn(agreementResponseMock);

			// Act
			final var exception = assertThrows(ThrowableProblem.class, () -> agreementService.getAgreementsByPartyIdAndCategories(municipalityId, partyId, filters, onlyActive));

			// Assert
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(agreementResponseMock));
			verify(agreementPartyProviderMock).getAgreementsByPartyIdAndCategories(municipalityId, partyId, filters, onlyActive);

			assertThat(exception.getStatus()).isEqualTo(NOT_FOUND);
			assertThat(exception.getMessage()).isEqualTo("Not Found: No matching agreements were found for party with id 'partyId'");
			assertThat(exception.getDetail()).isEqualTo("No matching agreements were found for party with id 'partyId'");
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {
		true, false
	})
	void getPagedAgreementsByPartyIdAndCategoriesOnlyActive(boolean onlyActive) {

		// Arrange
		final var municipalityId = "municipalityId";
		final var partyId = "partyId";
		final var filters = new ArrayList<Category>();

		final var parameters = new AgreementParameters();
		parameters.setOnlyActive(onlyActive);
		parameters.setPage(2);
		parameters.setLimit(3);

		final var responseMeta = new PagingAndSortingMetaData();
		responseMeta.setPage(2);
		responseMeta.setLimit(3);
		responseMeta.setCount(4);
		responseMeta.totalRecords(5L);
		responseMeta.totalPages(6);

		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			agreementMapperMock.when(() -> AgreementMapper.toAgreements(agreementResponseMock)).thenReturn(List.of(agreementMock));
			when(agreementPartyProviderMock.getAgreementsByPartyIdAndCategories(any(), any(), any(), anyInt(), anyInt(), any(), anyBoolean())).thenReturn(agreementResponseMock);
			when(agreementResponseMock.getMeta()).thenReturn(responseMeta);

			// Act
			final var result = agreementService.getPagedAgreementsByPartyIdAndCategories(municipalityId, partyId, filters, parameters);

			// Assert
			verify(agreementPartyProviderMock).getAgreementsByPartyIdAndCategories(municipalityId, partyId, filters, 2, 3, (onlyActive ? true : null), true);

			assertThat(result).isNotNull();
			assertThat(result.getAgreements()).hasSize(1).first().isSameAs(agreementMock);
			assertThat(result.getMetaData().getPage()).isEqualTo(2);
			assertThat(result.getMetaData().getLimit()).isEqualTo(3);
			assertThat(result.getMetaData().getCount()).isEqualTo(4);
			assertThat(result.getMetaData().getTotalRecords()).isEqualTo(5L);
			assertThat(result.getMetaData().getTotalPages()).isEqualTo(6);
		}
	}
}
