package se.sundsvall.agreement.service;

import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toCategories;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toCategory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import generated.se.sundsvall.datawarehousereader.AgreementResponse;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.integration.datawarehousereader.DataWarehouseReaderClient;

@Service
public class AgreementPartyProvider {
	private static final int DATAWAREHOUSEREADER_PAGE_LIMIT = 1000;
	private static final int DATAWAREHOUSEREADER_START_PAGE = 1;

	@Autowired
	private DataWarehouseReaderClient dataWarehouseReaderClient;

	public AgreementResponse getAgreementsByCategoryAndFacility(Category category, String facilityId) {
		return getAgreementsByCategoryAndFacility(category, facilityId, DATAWAREHOUSEREADER_START_PAGE, DATAWAREHOUSEREADER_PAGE_LIMIT);
	}

	private AgreementResponse getAgreementsByCategoryAndFacility(Category category, String facilityId, int page, int limit) {
		var agreementsResponse = dataWarehouseReaderClient.getAgreementsByCategoryAndFacility(toCategory(category), facilityId, page, limit);

		var currentPage = page;

		if (allNotNull(agreementsResponse, agreementsResponse.getMeta(), agreementsResponse.getMeta().getTotalPages()) && agreementsResponse.getMeta().getTotalPages() > currentPage) {
			while (agreementsResponse.getMeta().getTotalPages() > currentPage) {
				agreementsResponse.getAgreements().addAll(dataWarehouseReaderClient.getAgreementsByCategoryAndFacility(toCategory(category), facilityId, ++currentPage, limit).getAgreements());
			}
		}

		return agreementsResponse;
	}

	public AgreementResponse getAgreementsByPartyIdAndCategories(String partyId, List<Category> categories) {
		return getAgreementsByPartyIdAndCategories(partyId, categories, DATAWAREHOUSEREADER_START_PAGE, DATAWAREHOUSEREADER_PAGE_LIMIT);
	}

	private AgreementResponse getAgreementsByPartyIdAndCategories(String partyId, List<Category> categories, int page, int limit) {
		var agreementsResponse = dataWarehouseReaderClient.getAgreementsByPartyIdAndCategories(partyId, toCategories(categories), page, limit);

		var currentPage = page;

		if (allNotNull(agreementsResponse, agreementsResponse.getMeta(), agreementsResponse.getMeta().getTotalPages()) && agreementsResponse.getMeta().getTotalPages() > currentPage) {
			while (agreementsResponse.getMeta().getTotalPages() > currentPage) {
				agreementsResponse.getAgreements().addAll(dataWarehouseReaderClient.getAgreementsByPartyIdAndCategories(partyId, toCategories(categories), ++currentPage, limit).getAgreements());
			}
		}

		return agreementsResponse;
	}
}
