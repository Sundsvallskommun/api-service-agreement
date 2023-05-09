package se.sundsvall.agreement.service.mapper;

import static java.time.LocalDate.now;
import static java.time.ZoneId.systemDefault;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import generated.se.sundsvall.datawarehousereader.AgreementResponse;
import se.sundsvall.agreement.api.model.Agreement;
import se.sundsvall.agreement.api.model.AgreementParty;
import se.sundsvall.agreement.api.model.Category;

public class AgreementMapper {
	private AgreementMapper() {}

	public static List<generated.se.sundsvall.datawarehousereader.Category> toCategories(final List<Category> categories) {
		return ofNullable(categories).orElse(emptyList()).stream()
			.map(AgreementMapper::toCategory)
			.toList();
	}

	public static generated.se.sundsvall.datawarehousereader.Category toCategory(final Category category) {
		return switch (category) {
			case COMMUNICATION -> generated.se.sundsvall.datawarehousereader.Category.COMMUNICATION;
			case DISTRICT_HEATING -> generated.se.sundsvall.datawarehousereader.Category.DISTRICT_HEATING;
			case DISTRICT_COOLING -> generated.se.sundsvall.datawarehousereader.Category.DISTRICT_COOLING;
			case ELECTRICITY -> generated.se.sundsvall.datawarehousereader.Category.ELECTRICITY;
			case ELECTRICITY_TRADE -> generated.se.sundsvall.datawarehousereader.Category.ELECTRICITY_TRADE;
			case WASTE_MANAGEMENT -> generated.se.sundsvall.datawarehousereader.Category.WASTE_MANAGEMENT;
			case WATER -> generated.se.sundsvall.datawarehousereader.Category.WATER;
		};
	}

	public static List<AgreementParty> toAgreementParties(final AgreementResponse datawarehousereaderResponse) {
		if (responseIsEmpty(datawarehousereaderResponse)) {
			return new ArrayList<>();
		}

		final Map<String, AgreementParty> parties = new HashMap<>();

		datawarehousereaderResponse.getAgreements().forEach(agreement -> {
			if (!parties.containsKey(agreement.getCustomerNumber())) {
				parties.put(agreement.getCustomerNumber(), toAgreementParty(agreement));
			}

			parties.get(agreement.getCustomerNumber()).getAgreements().add(toAgreement(agreement));
		});

		return new ArrayList<>(parties.values());
	}

	private static boolean responseIsEmpty(final AgreementResponse response) {
		return isNull(response) || (response.getMeta().getTotalRecords() < 1);
	}

	private static AgreementParty toAgreementParty(final generated.se.sundsvall.datawarehousereader.Agreement agreement) {
		return AgreementParty.create()
			.withCustomerId(agreement.getCustomerNumber())
			.withAgreements(new ArrayList<>());
	}

	private static Agreement toAgreement(final generated.se.sundsvall.datawarehousereader.Agreement agreement) {
		return Agreement.create()
			.withActive(isActiveAgreement(agreement))
			.withAgreementId(agreement.getAgreementId())
			.withBillingId(agreement.getBillingId())
			.withBinding(toBoolean(agreement.getBinding()))
			.withBindingRule(agreement.getBindingRule())
			.withCategory(toCategory(agreement.getCategory()))
			.withDescription(agreement.getDescription())
			.withFacilityId(agreement.getFacilityId())
			.withFromDate(agreement.getFromDate())
			.withMainAgreement(toBoolean(agreement.getMainAgreement()))
			.withToDate(agreement.getToDate());
	}

	private static Category toCategory(final generated.se.sundsvall.datawarehousereader.Category category) {
		return switch (category) {
			case COMMUNICATION -> se.sundsvall.agreement.api.model.Category.COMMUNICATION;
			case DISTRICT_HEATING -> se.sundsvall.agreement.api.model.Category.DISTRICT_HEATING;
			case DISTRICT_COOLING -> se.sundsvall.agreement.api.model.Category.DISTRICT_COOLING;
			case ELECTRICITY -> se.sundsvall.agreement.api.model.Category.ELECTRICITY;
			case ELECTRICITY_TRADE -> se.sundsvall.agreement.api.model.Category.ELECTRICITY_TRADE;
			case WASTE_MANAGEMENT -> se.sundsvall.agreement.api.model.Category.WASTE_MANAGEMENT;
			case WATER -> se.sundsvall.agreement.api.model.Category.WATER;
		};
	}

	static boolean isActiveAgreement(final generated.se.sundsvall.datawarehousereader.Agreement agreement) {
		final boolean startDateHasOccured = nonNull(agreement.getFromDate()) && (now(systemDefault()).isEqual(agreement.getFromDate()) || now(systemDefault()).isAfter(agreement.getFromDate()));
		final boolean endDateHasNotOccured = isNull(agreement.getToDate()) || now(systemDefault()).isEqual(agreement.getToDate()) || now(systemDefault()).isBefore(agreement.getToDate());

		return startDateHasOccured && endDateHasNotOccured;
	}
}
