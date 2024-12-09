package se.sundsvall.agreement.service;

import static java.lang.String.format;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toAgreementParties;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toAgreements;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import se.sundsvall.agreement.api.model.AgreementParameters;
import se.sundsvall.agreement.api.model.AgreementResponse;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.api.model.PagedAgreementResponse;
import se.sundsvall.dept44.models.api.paging.PagingMetaData;

@Service
public class AgreementService {

	private static final String NO_CATEGORY_AND_FACILITY_MATCH_MESSAGE = "No matching agreements were found for facility with id '%s' and category '%s'";
	private static final String NO_PARTYID_MATCH_MESSAGE = "No matching agreements were found for party with id '%s'";
	private static final String NO_PARTYID_AND_CATEGORY_MATCH_MESSAGE = "No matching agreements were found for party with id '%s' and category in '%s'";

	private final AgreementPartyProvider agreementPartyProvider;

	public AgreementService(AgreementPartyProvider agreementPartyProvider) {
		this.agreementPartyProvider = agreementPartyProvider;
	}

	public AgreementResponse getAgreementsByCategoryAndFacilityId(String municipalityId, Category category, String facilityId, boolean onlyActive) {
		final var agreementParties = toAgreementParties(agreementPartyProvider.getAgreementsByCategoryAndFacility(municipalityId, category, facilityId, onlyActive));
		final var response = AgreementResponse.create().withAgreementParties(agreementParties);

		if (response.getAgreementParties().isEmpty()) {
			throw Problem.valueOf(NOT_FOUND, format(NO_CATEGORY_AND_FACILITY_MATCH_MESSAGE, facilityId, category));
		}

		return response;
	}

	public AgreementResponse getAgreementsByPartyIdAndCategories(final String municipalityId, final String partyId, final List<Category> categories, final boolean onlyActive) {
		final var agreementParties = toAgreementParties(agreementPartyProvider.getAgreementsByPartyIdAndCategories(municipalityId, partyId, categories, onlyActive));
		final var response = AgreementResponse.create().withAgreementParties(agreementParties);

		if (response.getAgreementParties().isEmpty()) {
			throw isEmpty(categories) ? Problem.valueOf(NOT_FOUND, format(NO_PARTYID_MATCH_MESSAGE, partyId)) : Problem.valueOf(NOT_FOUND, format(NO_PARTYID_AND_CATEGORY_MATCH_MESSAGE, partyId, categories));
		}

		return response;
	}

	public PagedAgreementResponse getPagedAgreementsByPartyIdAndCategories(final String municipalityId, final String partyId, final List<Category> categories, AgreementParameters parameters) {
		final var response = agreementPartyProvider.getAgreementsByPartyIdAndCategories(
			municipalityId,
			partyId,
			categories,
			parameters.getPage(),
			parameters.getLimit(),
			parameters.isOnlyActive() ? true : null,
			true);

		return PagedAgreementResponse.create()
			.withAgreements(toAgreements(response))
			.withMetaData(PagingMetaData.create()
				.withPage(response.getMeta().getPage())
				.withLimit(response.getMeta().getLimit())
				.withCount(response.getMeta().getCount())
				.withTotalRecords(response.getMeta().getTotalRecords())
				.withTotalPages(response.getMeta().getTotalPages()));
	}
}
