package se.sundsvall.agreement.service;

import static java.lang.String.format;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toAgreementParties;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toCategories;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toCategory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;

import se.sundsvall.agreement.api.model.AgreementParty;
import se.sundsvall.agreement.api.model.AgreementResponse;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.integration.datawarehousereader.DataWarehouseReaderClient;

@Service
public class AgreementService {

	private static final String NO_CATEGORY_AND_FACILITY_MATCH_MESSAGE = "No matching agreements were found for facility with id '%s' and category '%s'";
	private static final String NO_PARTYID_MATCH_MESSAGE = "No matching agreements were found for party with id '%s'";
	private static final String NO_PARTYID_AND_CATEGORY_MATCH_MESSAGE = "No matching agreements were found for party with id '%s' and category in '%s'";

	@Autowired
	private DataWarehouseReaderClient dataWarehouseReaderClient;

	public AgreementResponse getAgreementsByCategoryAndFacilityId(final Category category, final String facilityId, final boolean onlyActive) {

		final List<AgreementParty> agreementParties = toAgreementParties(dataWarehouseReaderClient.getAgreementsByCategoryAndFacility(toCategory(category), facilityId));
		final AgreementResponse response = AgreementResponse.create().withAgreementParties(onlyActive ? filterActiveParties(agreementParties) : agreementParties);
		if (response.getAgreementParties().isEmpty()) {
			throw Problem.valueOf(NOT_FOUND, format(NO_CATEGORY_AND_FACILITY_MATCH_MESSAGE, facilityId, category));
		}

		return response;
	}

	public AgreementResponse getAgreementsByPartyIdAndCategories(final String partyId, final List<Category> categories, final boolean onlyActive) {

		final List<AgreementParty> agreementParties = toAgreementParties(dataWarehouseReaderClient.getAgreementsByPartyIdAndCategories(partyId, toCategories(categories)));
		final AgreementResponse response = AgreementResponse.create().withAgreementParties(onlyActive ? filterActiveParties(agreementParties) : agreementParties);
		if (response.getAgreementParties().isEmpty()) {
			throw isEmpty(categories) ? Problem.valueOf(NOT_FOUND, format(NO_PARTYID_MATCH_MESSAGE, partyId)) : Problem.valueOf(NOT_FOUND, format(NO_PARTYID_AND_CATEGORY_MATCH_MESSAGE, partyId, categories));
		}

		return response;
	}

	private List<AgreementParty> filterActiveParties(final List<AgreementParty> parties) {
		return parties.stream()
			.map(this::removeNonActiveAgreements)
			.filter(party -> !party.getAgreements().isEmpty())
			.toList();
	}

	private AgreementParty removeNonActiveAgreements(final AgreementParty party) {
		party.getAgreements().removeIf(agreement -> !agreement.isActive());
		return party;
	}
}
