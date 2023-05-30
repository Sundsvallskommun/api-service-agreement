package se.sundsvall.agreement.service;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.agreement.api.model.Category.WASTE_MANAGEMENT;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import generated.se.sundsvall.datawarehousereader.Agreement;
import generated.se.sundsvall.datawarehousereader.AgreementResponse;
import generated.se.sundsvall.datawarehousereader.MetaData;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.integration.datawarehousereader.DataWarehouseReaderClient;

@ExtendWith(MockitoExtension.class)
class AgreementPartyProviderTest {

	@Mock
	private DataWarehouseReaderClient dataWarehouseReaderClientMock;

	@Mock
	private generated.se.sundsvall.datawarehousereader.AgreementResponse agreementResponseMock;
	
	@Mock
	private Agreement agreementMock;

	@Mock
	private MetaData metaDataMock;
	
	@InjectMocks
	private AgreementPartyProvider agreementPartyProvider;

	@Test
	void getAgreementsByCategoryAndFacilityIdWhenAgreementResponseHasOnePage() {
		final var facilityId = "facilityId";
		
		// Setup mocks
		when(dataWarehouseReaderClientMock.getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId, 1, 1000)).thenReturn(agreementResponseMock);
		when(agreementResponseMock.getMeta()).thenReturn(metaDataMock);
		when(agreementResponseMock.getAgreements()).thenReturn(List.of(agreementMock));
		when(metaDataMock.getTotalPages()).thenReturn(1);

		// Call
		final var response = agreementPartyProvider.getAgreementsByCategoryAndFacility(WASTE_MANAGEMENT, facilityId);

		// Verifications
		verify(dataWarehouseReaderClientMock).getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId, 1, 1000);
		assertThat(response).isNotNull().extracting(AgreementResponse::getAgreements).asList().hasSize(1);
	}
	
	@Test
	void getAgreementsByCategoryAndFacilityIdWhenAgreementResponseHasMoreThanOnePage() {
		final var facilityId = "facilityId";

		// Setup mocks
		when(dataWarehouseReaderClientMock.getAgreementsByCategoryAndFacility(any(), any(), anyInt(), anyInt())).thenReturn(agreementResponseMock);
		when(agreementResponseMock.getMeta()).thenReturn(metaDataMock);
		when(metaDataMock.getTotalPages()).thenReturn(3);

		// Call
		agreementPartyProvider.getAgreementsByCategoryAndFacility(WASTE_MANAGEMENT, facilityId);

		// Verifications
		verify(dataWarehouseReaderClientMock).getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId, 1, 1000);
		verify(dataWarehouseReaderClientMock).getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId, 2, 1000);
		verify(dataWarehouseReaderClientMock).getAgreementsByCategoryAndFacility(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT, facilityId, 3, 1000);
		verifyNoMoreInteractions(dataWarehouseReaderClientMock);
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesWhenAgreementResponseHasOnePage() {
		final var partyId = "partyId";
		final var categories = List.of(WASTE_MANAGEMENT);
		
		// Setup mocks
		when(dataWarehouseReaderClientMock.getAgreementsByPartyIdAndCategories(eq(partyId), any(), anyInt(), anyInt())).thenReturn(agreementResponseMock);
		when(agreementResponseMock.getMeta()).thenReturn(metaDataMock);
		when(agreementResponseMock.getAgreements()).thenReturn(List.of(agreementMock));
		when(metaDataMock.getTotalPages()).thenReturn(1);

		// Call
		final var response = agreementPartyProvider.getAgreementsByPartyIdAndCategories(partyId, categories);

		// Verifications
		verify(dataWarehouseReaderClientMock).getAgreementsByPartyIdAndCategories(partyId, List.of(generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT), 1, 1000);
		assertThat(response).isNotNull().extracting(AgreementResponse::getAgreements).asList().hasSize(1);
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesWhenAgreementResponseHasMoreThanOnePage() {
		final var partyId = "partyId";
		final List<Category> categories = emptyList();
		
		// Setup mocks
		when(dataWarehouseReaderClientMock.getAgreementsByPartyIdAndCategories(eq(partyId), any(), anyInt(), anyInt())).thenReturn(agreementResponseMock);
		when(agreementResponseMock.getMeta()).thenReturn(metaDataMock);
		when(metaDataMock.getTotalPages()).thenReturn(3);

		// Call
		agreementPartyProvider.getAgreementsByPartyIdAndCategories(partyId, categories);

		// Verifications
		verify(dataWarehouseReaderClientMock).getAgreementsByPartyIdAndCategories(partyId, emptyList(), 1, 1000);
		verify(dataWarehouseReaderClientMock).getAgreementsByPartyIdAndCategories(partyId, emptyList(), 2, 1000);
		verify(dataWarehouseReaderClientMock).getAgreementsByPartyIdAndCategories(partyId, emptyList(), 3, 1000);
		verifyNoMoreInteractions(dataWarehouseReaderClientMock);
	}
}
