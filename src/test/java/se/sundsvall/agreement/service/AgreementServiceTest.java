package se.sundsvall.agreement.service;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.agreement.api.model.Category.WASTE_MANAGEMENT;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;

import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.api.model.Agreement;
import se.sundsvall.agreement.api.model.AgreementParty;
import se.sundsvall.agreement.api.model.AgreementResponse;
import se.sundsvall.agreement.integration.datawarehousereader.DataWarehouseReaderClient;
import se.sundsvall.agreement.service.mapper.AgreementMapper;

@ExtendWith(MockitoExtension.class)
class AgreementServiceTest {

	@Mock
	private DataWarehouseReaderClient dataWarehouseReaderClientMock;

	@Mock
	private generated.se.sundsvall.datawarehousereader.AgreementResponse agreementResponseMock;
	
	@Mock
	private AgreementParty agreementPartyMock;
	
	@Mock
	private Agreement agreementMock;
	
	@InjectMocks
	private AgreementService agreementService;

	@Test
	void getOnlyActiveAgreementsByCategoryAndFacilityIdShouldReturnAgreement() {
		final var facilityId = "facilityId";
		
		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			// Setup mocks
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(agreementResponseMock)).thenReturn(new ArrayList<>(of(agreementPartyMock)));
			agreementMapperMock.when(() -> AgreementMapper.toCategory(any())).thenCallRealMethod();
			when(dataWarehouseReaderClientMock.getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId)).thenReturn(agreementResponseMock);
			when(agreementPartyMock.getAgreements()).thenReturn(new ArrayList<>(of(agreementMock)));
			when(agreementMock.isActive()).thenReturn(true);

			// Call
			AgreementResponse response = agreementService.getAgreementsByCategoryAndFacilityId(WASTE_MANAGEMENT, facilityId, true);

			// Verifications
			agreementMapperMock.verify(() -> AgreementMapper.toCategory(WASTE_MANAGEMENT));
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(agreementResponseMock));
			verify(agreementPartyMock, times(2)).getAgreements();
			verify(agreementMock).isActive();
			
			assertThat(response).isNotNull().extracting(AgreementResponse::getAgreementParties).asList().hasSize(1);
			assertThat(response.getAgreementParties()).extracting(AgreementParty::getAgreements).asList().hasSize(1);
		}
	}
	
	@Test
	void getOnlyActiveAgreementsByCategoryAndFacilityIdWhenNoActiveExists() {
		final var facilityId = "facilityId";
		
		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			// Setup mocks
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(agreementResponseMock)).thenReturn(new ArrayList<>(of(agreementPartyMock)));
			agreementMapperMock.when(() -> AgreementMapper.toCategory(any())).thenCallRealMethod();
			when(dataWarehouseReaderClientMock.getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId)).thenReturn(agreementResponseMock);
			when(agreementPartyMock.getAgreements()).thenReturn(new ArrayList<>(of(agreementMock)));

			// Call
			final var exception = assertThrows(ThrowableProblem.class, () -> agreementService.getAgreementsByCategoryAndFacilityId(WASTE_MANAGEMENT, facilityId, true));

			// Verifications
			agreementMapperMock.verify(() -> AgreementMapper.toCategory(WASTE_MANAGEMENT));
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(agreementResponseMock));
			verify(agreementPartyMock, times(2)).getAgreements();
			verify(agreementMock).isActive();
			
			assertThat(exception.getStatus().getStatusCode()).isEqualTo(NOT_FOUND.getStatusCode());
			assertThat(exception.getStatus().getReasonPhrase()).isEqualTo(NOT_FOUND.getReasonPhrase());
			assertThat(exception.getMessage()).isEqualTo("Not Found: No matching agreements were found for facility with id 'facilityId' and category 'WASTE_MANAGEMENT'");
			assertThat(exception.getDetail()).isEqualTo("No matching agreements were found for facility with id 'facilityId' and category 'WASTE_MANAGEMENT'");
		}
	}

	@Test
	void getAllAgreementsByCategoryAndFacilityIdShouldReturnAgreement() {
		final var facilityId = "facilityId";
		
		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			// Setup mocks
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(agreementResponseMock)).thenReturn(new ArrayList<>(of(agreementPartyMock)));
			agreementMapperMock.when(() -> AgreementMapper.toCategory(any())).thenCallRealMethod();
			when(dataWarehouseReaderClientMock.getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId)).thenReturn(agreementResponseMock);
			when(agreementPartyMock.getAgreements()).thenReturn(new ArrayList<>(of(agreementMock)));

			// Call
			AgreementResponse response = agreementService.getAgreementsByCategoryAndFacilityId(WASTE_MANAGEMENT, facilityId, false);

			// Verifications
			agreementMapperMock.verify(() -> AgreementMapper.toCategory(WASTE_MANAGEMENT));
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(agreementResponseMock));
			verifyNoInteractions(agreementPartyMock, agreementMock);
			
			assertThat(response).isNotNull().extracting(AgreementResponse::getAgreementParties).asList().hasSize(1);
			assertThat(response.getAgreementParties()).extracting(AgreementParty::getAgreements).asList().hasSize(1);
		}
	}
	
	@Test
	void getAllAgreementsByCategoryAndFacilityIdWhenNoAgreementsExists() {
		final var facilityId = "facilityId";
		
		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			// Setup mocks
			agreementMapperMock.when(() -> AgreementMapper.toCategory(any())).thenCallRealMethod();
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(any())).thenReturn(emptyList());

			// Call
			final var exception = assertThrows(ThrowableProblem.class, () -> agreementService.getAgreementsByCategoryAndFacilityId(WASTE_MANAGEMENT, facilityId, false));

			// Verifications
			agreementMapperMock.verify(() -> AgreementMapper.toCategory(WASTE_MANAGEMENT));
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(any()));
			verifyNoInteractions(agreementPartyMock, agreementMock);
			
			assertThat(exception.getStatus().getStatusCode()).isEqualTo(NOT_FOUND.getStatusCode());
			assertThat(exception.getStatus().getReasonPhrase()).isEqualTo(NOT_FOUND.getReasonPhrase());
			assertThat(exception.getMessage()).isEqualTo("Not Found: No matching agreements were found for facility with id 'facilityId' and category 'WASTE_MANAGEMENT'");
			assertThat(exception.getDetail()).isEqualTo("No matching agreements were found for facility with id 'facilityId' and category 'WASTE_MANAGEMENT'");
		}
	}
	
	@Test
	void getAgreementsByPartyIdAndCategoriesWithoutCategoryFiltersAndOnlyActiveTrueShouldReturnAgreement() {
		final var partyId = "partyId";
		final List<Category> filters = emptyList();
		final var onlyActive = true;
		
		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			// Setup mocks
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(agreementResponseMock)).thenReturn(new ArrayList<>(of(agreementPartyMock)));
			agreementMapperMock.when(() -> AgreementMapper.toCategories(any())).thenCallRealMethod();
			when(dataWarehouseReaderClientMock.getAgreementsByPartyIdAndCategories(eq(partyId), any())).thenReturn(agreementResponseMock);
			when(agreementPartyMock.getAgreements()).thenReturn(new ArrayList<>(of(agreementMock)));
			when(agreementMock.isActive()).thenReturn(true);

			// Call
			AgreementResponse response = agreementService.getAgreementsByPartyIdAndCategories(partyId, filters, onlyActive);

			// Verifications
			agreementMapperMock.verify(() -> AgreementMapper.toCategories(filters));
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(agreementResponseMock));
			verify(agreementPartyMock, times(2)).getAgreements();
			verify(agreementMock).isActive();
			
			assertThat(response).isNotNull().extracting(AgreementResponse::getAgreementParties).asList().hasSize(1);
			assertThat(response.getAgreementParties()).extracting(AgreementParty::getAgreements).asList().hasSize(1);
		}
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesWithoutCategoryFiltersAndOnlyActiveTrueWhenNoActiveExists() {
		final var partyId = "partyId";
		final List<Category> filters = emptyList();
		final var onlyActive = true;
		
		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			// Setup mocks
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(agreementResponseMock)).thenReturn(new ArrayList<>(of(agreementPartyMock)));
			agreementMapperMock.when(() -> AgreementMapper.toCategories(any())).thenCallRealMethod();
			when(dataWarehouseReaderClientMock.getAgreementsByPartyIdAndCategories(eq(partyId), any())).thenReturn(agreementResponseMock);
			when(agreementPartyMock.getAgreements()).thenReturn(new ArrayList<>(of(agreementMock)));

			// Call
			final var exception = assertThrows(ThrowableProblem.class, () -> agreementService.getAgreementsByPartyIdAndCategories(partyId, filters, onlyActive));

			// Verifications
			agreementMapperMock.verify(() -> AgreementMapper.toCategories(filters));
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(agreementResponseMock));
			verify(agreementPartyMock, times(2)).getAgreements();
			verify(agreementMock).isActive();
			
			assertThat(exception.getStatus().getStatusCode()).isEqualTo(NOT_FOUND.getStatusCode());
			assertThat(exception.getStatus().getReasonPhrase()).isEqualTo(NOT_FOUND.getReasonPhrase());
			assertThat(exception.getMessage()).isEqualTo("Not Found: No matching agreements were found for party with id 'partyId'");
			assertThat(exception.getDetail()).isEqualTo("No matching agreements were found for party with id 'partyId'");
		}
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesWithCategoryFilterAndOnlyActiveTrueWhenNoActiveExists() {
		final var partyId = "partyId";
		final List<Category> filters = List.of(Category.WASTE_MANAGEMENT);
		final var onlyActive = true;
		
		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			// Setup mocks
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(agreementResponseMock)).thenReturn(new ArrayList<>(of(agreementPartyMock)));
			agreementMapperMock.when(() -> AgreementMapper.toCategories(any())).thenCallRealMethod();
			when(dataWarehouseReaderClientMock.getAgreementsByPartyIdAndCategories(eq(partyId), any())).thenReturn(agreementResponseMock);
			when(agreementPartyMock.getAgreements()).thenReturn(new ArrayList<>(of(agreementMock)));

			// Call
			final var exception = assertThrows(ThrowableProblem.class, () -> agreementService.getAgreementsByPartyIdAndCategories(partyId, filters, onlyActive));

			// Verifications
			agreementMapperMock.verify(() -> AgreementMapper.toCategories(filters));
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(agreementResponseMock));
			verify(agreementPartyMock, times(2)).getAgreements();
			verify(agreementMock).isActive();
			
			assertThat(exception.getStatus().getStatusCode()).isEqualTo(NOT_FOUND.getStatusCode());
			assertThat(exception.getStatus().getReasonPhrase()).isEqualTo(NOT_FOUND.getReasonPhrase());
			assertThat(exception.getMessage()).isEqualTo("Not Found: No matching agreements were found for party with id 'partyId' and category in '[WASTE_MANAGEMENT]'");
			assertThat(exception.getDetail()).isEqualTo("No matching agreements were found for party with id 'partyId' and category in '[WASTE_MANAGEMENT]'");
		}
	}
	
	@Test
	void getAllAgreementsByPartyIdAndCategoriesWithCategoryFilterAndOnlyActiveTrueShouldReturnAgreement() {
		final var partyId = "partyId";
		final List<Category> filters = List.of(Category.WASTE_MANAGEMENT);
		final var onlyActive = false;
		
		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			// Setup mocks
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(agreementResponseMock)).thenReturn(new ArrayList<>(of(agreementPartyMock)));
			agreementMapperMock.when(() -> AgreementMapper.toCategories(any())).thenCallRealMethod();
			when(dataWarehouseReaderClientMock.getAgreementsByPartyIdAndCategories(eq(partyId), any())).thenReturn(agreementResponseMock);
			when(agreementPartyMock.getAgreements()).thenReturn(new ArrayList<>(of(agreementMock)));

			// Call
			AgreementResponse response = agreementService.getAgreementsByPartyIdAndCategories(partyId, filters, onlyActive);

			// Verifications
			agreementMapperMock.verify(() -> AgreementMapper.toCategories(filters));
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(agreementResponseMock));
			verify(agreementPartyMock, never()).getAgreements();
			verify(agreementMock, never()).isActive();
			
			assertThat(response).isNotNull().extracting(AgreementResponse::getAgreementParties).asList().hasSize(1);
			assertThat(response.getAgreementParties()).extracting(AgreementParty::getAgreements).asList().hasSize(1);
		}
	}
	
	@Test
	void getAllAgreementsByPartyIdAndCategoriesWithCategoryWhenNoAgreementsExists() {
		final var partyId = "partyId";
		final List<Category> filters = List.of(WASTE_MANAGEMENT);
		
		try (MockedStatic<AgreementMapper> agreementMapperMock = Mockito.mockStatic(AgreementMapper.class)) {
			// Setup mocks
			agreementMapperMock.when(() -> AgreementMapper.toCategories(any())).thenCallRealMethod();
			agreementMapperMock.when(() -> AgreementMapper.toAgreementParties(any())).thenReturn(emptyList());

			// Call
			final var exception = assertThrows(ThrowableProblem.class, () -> agreementService.getAgreementsByPartyIdAndCategories(partyId, filters, true));

			// Verifications
			agreementMapperMock.verify(() -> AgreementMapper.toCategories(filters));
			agreementMapperMock.verify(() -> AgreementMapper.toAgreementParties(any()));
			verifyNoInteractions(agreementPartyMock, agreementMock);
			
			assertThat(exception.getStatus().getStatusCode()).isEqualTo(NOT_FOUND.getStatusCode());
			assertThat(exception.getStatus().getReasonPhrase()).isEqualTo(NOT_FOUND.getReasonPhrase());
			assertThat(exception.getMessage()).isEqualTo("Not Found: No matching agreements were found for party with id 'partyId' and category in '[WASTE_MANAGEMENT]'");
			assertThat(exception.getDetail()).isEqualTo("No matching agreements were found for party with id 'partyId' and category in '[WASTE_MANAGEMENT]'");
		}
	}
}
