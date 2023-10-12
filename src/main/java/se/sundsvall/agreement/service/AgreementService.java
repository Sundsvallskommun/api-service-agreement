package se.sundsvall.agreement.service;

import static java.lang.String.format;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toAgreementParties;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;

import se.sundsvall.agreement.api.model.AgreementParty;
import se.sundsvall.agreement.api.model.AgreementResponse;
import se.sundsvall.agreement.api.model.Category;

@Service
public class AgreementService {

	private static final String NO_CATEGORY_AND_FACILITY_MATCH_MESSAGE = "No matching agreements were found for facility with id '%s' and category '%s'";
	private static final String NO_PARTYID_MATCH_MESSAGE = "No matching agreements were found for party with id '%s'";
	private static final String NO_PARTYID_AND_CATEGORY_MATCH_MESSAGE = "No matching agreements were found for party with id '%s' and category in '%s'";

	@Autowired
	private AgreementPartyProvider agreementPartyProvider;


	public AgreementResponse getAgreementsByCategoryAndFacilityId(Category category, String facilityId, boolean onlyActive) {
		var agreementParties = toAgreementParties(agreementPartyProvider.getAgreementsByCategoryAndFacility(category, facilityId, onlyActive));
		var response = AgreementResponse.create().withAgreementParties(agreementParties);

		if (response.getAgreementParties().isEmpty()) {
			throw Problem.valueOf(NOT_FOUND, format(NO_CATEGORY_AND_FACILITY_MATCH_MESSAGE, facilityId, category));
		}

		return response;
	}

	public AgreementResponse getAgreementsByPartyIdAndCategories(final String partyId, final List<Category> categories, final boolean onlyActive) {
		var agreementParties = toAgreementParties(agreementPartyProvider.getAgreementsByPartyIdAndCategories(partyId, categories, onlyActive));
		var response = AgreementResponse.create().withAgreementParties(agreementParties);

		if (response.getAgreementParties().isEmpty()) {
			throw isEmpty(categories) ? Problem.valueOf(NOT_FOUND, format(NO_PARTYID_MATCH_MESSAGE, partyId)) : Problem.valueOf(NOT_FOUND, format(NO_PARTYID_AND_CATEGORY_MATCH_MESSAGE, partyId, categories));
		}

		return response;
	}
}
