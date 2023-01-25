package se.sundsvall.agreement.service.mapper;

import static java.util.Optional.ofNullable;
import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.api.model.Agreement;
import se.sundsvall.agreement.api.model.AgreementParty;

public class AgreementMapper {
	private AgreementMapper() {}
	
	public static List<generated.se.sundsvall.datawarehousereader.Category> toCategories(List<Category> categories) {
		return ofNullable(categories).orElse(emptyList()).stream()
			.map(AgreementMapper::toCategory)
			.toList();
	}
	
	public static generated.se.sundsvall.datawarehousereader.Category toCategory(Category category) {
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

	public static List<AgreementParty> toAgreementParties(generated.se.sundsvall.datawarehousereader.AgreementResponse datawarehousereaderResponse) {
		if (responseIsEmpty(datawarehousereaderResponse)) return emptyList();

		Map<String, AgreementParty> parties = new HashMap<>();
		
		datawarehousereaderResponse.getAgreements().forEach(agreement -> {
			if (!parties.containsKey(agreement.getCustomerNumber())) {
				parties.put(agreement.getCustomerNumber(), toAgreementParty(agreement));
			}
			
			parties.get(agreement.getCustomerNumber()).getAgreements().add(toAgreement(agreement));
		});
		
		return new ArrayList<>(parties.values());
	}

	private static boolean responseIsEmpty(generated.se.sundsvall.datawarehousereader.AgreementResponse response) {
		return isNull(response) || response.getMeta().getTotalRecords() < 1;
	}
	
	private static AgreementParty toAgreementParty(generated.se.sundsvall.datawarehousereader.Agreement agreement) {
		return AgreementParty.create()
			.withCustomerId(agreement.getCustomerNumber())
			.withAgreements(new ArrayList<>());
	}
	
	private static Agreement toAgreement(generated.se.sundsvall.datawarehousereader.Agreement agreement) {
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
	
	private static Category toCategory(generated.se.sundsvall.datawarehousereader.Category category) {
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

	static boolean isActiveAgreement(generated.se.sundsvall.datawarehousereader.Agreement agreement) {
		boolean startDateHasOccured = nonNull(agreement.getFromDate()) && (now().isEqual(agreement.getFromDate()) || now().isAfter(agreement.getFromDate()));
		boolean endDateHasNotOccured = isNull(agreement.getToDate()) || now().isEqual(agreement.getToDate()) || now().isBefore(agreement.getToDate());

		return startDateHasOccured && endDateHasNotOccured;
	}
}
