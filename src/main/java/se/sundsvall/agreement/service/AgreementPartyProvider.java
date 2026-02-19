package se.sundsvall.agreement.service;

import generated.se.sundsvall.datawarehousereader.AgreementResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.integration.datawarehousereader.DataWarehouseReaderClient;

import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toCategories;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toCategory;

@Service
public class AgreementPartyProvider {

	private static final int DATAWAREHOUSEREADER_PAGE_LIMIT = 1000;
	private static final int DATAWAREHOUSEREADER_START_PAGE = 1;

	private final DataWarehouseReaderClient dataWarehouseReaderClient;

	public AgreementPartyProvider(DataWarehouseReaderClient dataWarehouseReaderClient) {
		this.dataWarehouseReaderClient = dataWarehouseReaderClient;
	}

	public AgreementResponse getAgreementsByCategoryAndFacility(String municipalityId, Category category, String facilityId, boolean onlyActive) {
		final var active = onlyActive ? true : null;
		return getAgreementsByCategoryAndFacility(municipalityId, category, facilityId, DATAWAREHOUSEREADER_START_PAGE, DATAWAREHOUSEREADER_PAGE_LIMIT, active);
	}

	private AgreementResponse getAgreementsByCategoryAndFacility(String municipalityId, Category category, String facilityId, int page, int limit, Boolean active) {
		final var agreementsResponse = dataWarehouseReaderClient.getAgreementsByCategoryAndFacility(municipalityId, toCategory(category), facilityId, page, limit, active);

		var currentPage = page;

		if (allNotNull(agreementsResponse, agreementsResponse.getMeta(), agreementsResponse.getMeta().getTotalPages()) && (agreementsResponse.getMeta().getTotalPages() > currentPage)) {
			while (agreementsResponse.getMeta().getTotalPages() > currentPage) {
				agreementsResponse.getAgreements().addAll(dataWarehouseReaderClient.getAgreementsByCategoryAndFacility(municipalityId, toCategory(category), facilityId, ++currentPage, limit, active).getAgreements());
			}
		}

		return agreementsResponse;
	}

	public AgreementResponse getAgreementsByPartyIdAndCategories(String municipalityId, String partyId, List<Category> categories, boolean onlyActive) {
		final var active = onlyActive ? true : null;
		return getAgreementsByPartyIdAndCategories(municipalityId, partyId, categories, DATAWAREHOUSEREADER_START_PAGE, DATAWAREHOUSEREADER_PAGE_LIMIT, active, false);
	}

	public AgreementResponse getAgreementsByPartyIdAndCategories(String municipalityId, String partyId, List<Category> categories, int page, int limit, Boolean active, boolean paged) {
		final var agreementsResponse = dataWarehouseReaderClient.getAgreementsByPartyIdAndCategories(municipalityId, partyId, toCategories(categories), page, limit, active);

		var currentPage = page;

		if (!paged && allNotNull(agreementsResponse, agreementsResponse.getMeta(), agreementsResponse.getMeta().getTotalPages()) && (agreementsResponse.getMeta().getTotalPages() > currentPage)) {
			while (agreementsResponse.getMeta().getTotalPages() > currentPage) {
				agreementsResponse.getAgreements().addAll(dataWarehouseReaderClient.getAgreementsByPartyIdAndCategories(municipalityId, partyId, toCategories(categories), ++currentPage, limit, active).getAgreements());
			}
		}

		return agreementsResponse;
	}
}
