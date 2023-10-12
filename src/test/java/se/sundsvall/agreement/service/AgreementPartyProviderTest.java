package se.sundsvall.agreement.service;

import generated.se.sundsvall.datawarehousereader.Agreement;
import generated.se.sundsvall.datawarehousereader.AgreementResponse;
import generated.se.sundsvall.datawarehousereader.PagingAndSortingMetaData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.integration.datawarehousereader.DataWarehouseReaderClient;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.agreement.api.model.Category.WASTE_MANAGEMENT;

@ExtendWith(MockitoExtension.class)
class AgreementPartyProviderTest {

	@Mock
	private DataWarehouseReaderClient dataWarehouseReaderClientMock;

	@Mock
	private generated.se.sundsvall.datawarehousereader.AgreementResponse agreementResponseMock;
	
	@Mock
	private Agreement agreementMock;

	@Mock
	private PagingAndSortingMetaData metaDataMock;
	
	@InjectMocks
	private AgreementPartyProvider agreementPartyProvider;

	@Test
	void getAgreementsByCategoryAndFacilityIdWhenAgreementResponseHasOnePage() {
		final var facilityId = "facilityId";
		
		// Setup mocks
		when(dataWarehouseReaderClientMock.getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId, 1, 1000, true)).thenReturn(agreementResponseMock);
		when(agreementResponseMock.getMeta()).thenReturn(metaDataMock);
		when(agreementResponseMock.getAgreements()).thenReturn(List.of(agreementMock));
		when(metaDataMock.getTotalPages()).thenReturn(1);

		// Call
		final var response = agreementPartyProvider.getAgreementsByCategoryAndFacility(WASTE_MANAGEMENT, facilityId, true);

		// Verifications
		verify(dataWarehouseReaderClientMock).getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId, 1, 1000, true);
		assertThat(response).isNotNull().extracting(AgreementResponse::getAgreements).asList().hasSize(1).first().isSameAs(agreementMock);
	}
	
	@Test
	void getAgreementsByCategoryAndFacilityIdWhenAgreementResponseHasMoreThanOnePage() {
		final var facilityId = "facilityId";

		// Setup mocks
		when(dataWarehouseReaderClientMock.getAgreementsByCategoryAndFacility(any(), any(), anyInt(), anyInt(), any())).thenReturn(
			new AgreementResponse().agreements(new ArrayList<>(List.of(agreementMock))).meta(metaDataMock),
			new AgreementResponse().agreements(new ArrayList<>(List.of(agreementMock))).meta(metaDataMock),
			new AgreementResponse().agreements(new ArrayList<>(List.of(agreementMock))).meta(metaDataMock));
		when(metaDataMock.getTotalPages()).thenReturn(3);

		// Call
		var response = agreementPartyProvider.getAgreementsByCategoryAndFacility(WASTE_MANAGEMENT, facilityId, false);

		// Verifications
		verify(dataWarehouseReaderClientMock).getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId, 1, 1000, null);
		verify(dataWarehouseReaderClientMock).getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId, 2, 1000, null);
		verify(dataWarehouseReaderClientMock).getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId, 3, 1000, null);
		verifyNoMoreInteractions(dataWarehouseReaderClientMock);
		assertThat(response).isNotNull().extracting(AgreementResponse::getAgreements).asList().hasSize(3).hasSameElementsAs(List.of(agreementMock, agreementMock, agreementMock));
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesWhenAgreementResponseHasOnePage() {
		final var partyId = "partyId";
		final var categories = List.of(WASTE_MANAGEMENT);
		final var onlyActive = true;
		
		// Setup mocks
		when(dataWarehouseReaderClientMock.getAgreementsByPartyIdAndCategories(eq(partyId), any(), anyInt(), anyInt(), anyBoolean())).thenReturn(agreementResponseMock);
		when(agreementResponseMock.getMeta()).thenReturn(metaDataMock);
		when(agreementResponseMock.getAgreements()).thenReturn(List.of(agreementMock));
		when(metaDataMock.getTotalPages()).thenReturn(1);

		// Call
		final var response = agreementPartyProvider.getAgreementsByPartyIdAndCategories(partyId, categories, onlyActive);

		// Verifications
		verify(dataWarehouseReaderClientMock).getAgreementsByPartyIdAndCategories(partyId, List.of(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT), 1, 1000, onlyActive);
		assertThat(response).isNotNull().extracting(AgreementResponse::getAgreements).asList().hasSize(1).first().isSameAs(agreementMock);
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesWhenAgreementResponseHasMoreThanOnePage() {
		final var partyId = "partyId";
		final List<Category> categories = emptyList();
		
		// Setup mocks
		when(dataWarehouseReaderClientMock.getAgreementsByPartyIdAndCategories(eq(partyId), any(), anyInt(), anyInt(), any())).thenReturn(
			new AgreementResponse().agreements(new ArrayList<>(List.of(agreementMock))).meta(metaDataMock),
			new AgreementResponse().agreements(new ArrayList<>(List.of(agreementMock))).meta(metaDataMock),
			new AgreementResponse().agreements(new ArrayList<>(List.of(agreementMock))).meta(metaDataMock));
		when(metaDataMock.getTotalPages()).thenReturn(3);

		// Call
		var response = agreementPartyProvider.getAgreementsByPartyIdAndCategories(partyId, categories, false);

		// Verifications
		verify(dataWarehouseReaderClientMock).getAgreementsByPartyIdAndCategories(partyId, emptyList(), 1, 1000, null);
		verify(dataWarehouseReaderClientMock).getAgreementsByPartyIdAndCategories(partyId, emptyList(), 2, 1000, null);
		verify(dataWarehouseReaderClientMock).getAgreementsByPartyIdAndCategories(partyId, emptyList(), 3, 1000, null);
		verifyNoMoreInteractions(dataWarehouseReaderClientMock);
		assertThat(response).isNotNull().extracting(AgreementResponse::getAgreements).asList().hasSize(3).hasSameElementsAs(List.of(agreementMock, agreementMock, agreementMock));
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesPaged() {
		final var partyId = "partyId";
		final List<Category> categories = List.of(WASTE_MANAGEMENT);
		final var page = 2;
		final var limit = 13;
		final var active = Boolean.TRUE;

		// Setup mocks
		when(dataWarehouseReaderClientMock.getAgreementsByPartyIdAndCategories(eq(partyId), any(), anyInt(), anyInt(), any())).thenReturn(agreementResponseMock);

		// Call
		var response = agreementPartyProvider.getAgreementsByPartyIdAndCategories(partyId, categories, page, limit, active, true);

		verify(dataWarehouseReaderClientMock).getAgreementsByPartyIdAndCategories(
			same(partyId),
			eq(List.of(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT)),
			eq(page),
			eq(limit),
			same(active));
		assertThat(response).isSameAs(agreementResponseMock);
	}
}
