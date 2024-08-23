package se.sundsvall.agreement.service.mapper;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static se.sundsvall.agreement.api.model.Category.COMMUNICATION;
import static se.sundsvall.agreement.api.model.Category.DISTRICT_COOLING;
import static se.sundsvall.agreement.api.model.Category.DISTRICT_HEATING;
import static se.sundsvall.agreement.api.model.Category.ELECTRICITY;
import static se.sundsvall.agreement.api.model.Category.ELECTRICITY_TRADE;
import static se.sundsvall.agreement.api.model.Category.WASTE_MANAGEMENT;
import static se.sundsvall.agreement.api.model.Category.WATER;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toAgreementParties;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toAgreements;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toCategories;
import static se.sundsvall.agreement.service.mapper.AgreementMapper.toCategory;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import generated.se.sundsvall.datawarehousereader.Agreement;
import generated.se.sundsvall.datawarehousereader.Category;
import generated.se.sundsvall.datawarehousereader.PagingAndSortingMetaData;
import se.sundsvall.agreement.api.model.AgreementParty;

class AgreementMapperTest {

	private static final String AGREEMENT_ID = "agreementId";
	private static final String BILLING_ID = "billingId";
	private static final String BINDING_RULE = "bindingRule";
	private static final String CUSTOMER_ID = "customerId";
	private static final String DESCRIPTION = "description";
	private static final String FACILITY_ID = "facilityId";
	private static final String PARTY_ID = "partyId";

	@Test
	void testToCategories() {
		assertThat(toCategories(null)).isEmpty();
		assertThat(toCategories(emptyList())).isEmpty();
		assertThat(toCategories(List.of(COMMUNICATION, DISTRICT_COOLING, DISTRICT_HEATING, ELECTRICITY, ELECTRICITY_TRADE, WASTE_MANAGEMENT, WATER)))
			.containsExactlyInAnyOrder(
				Category.COMMUNICATION,
				Category.DISTRICT_COOLING,
				Category.DISTRICT_HEATING,
				Category.ELECTRICITY,
				Category.ELECTRICITY_TRADE,
				Category.WASTE_MANAGEMENT,
				Category.WATER);
	}

	@Test
	void testToCategory() {
		assertThat(toCategory(COMMUNICATION)).isEqualTo(Category.COMMUNICATION);
		assertThat(toCategory(DISTRICT_COOLING)).isEqualTo(Category.DISTRICT_COOLING);
		assertThat(toCategory(DISTRICT_HEATING)).isEqualTo(Category.DISTRICT_HEATING);
		assertThat(toCategory(ELECTRICITY)).isEqualTo(Category.ELECTRICITY);
		assertThat(toCategory(ELECTRICITY_TRADE)).isEqualTo(Category.ELECTRICITY_TRADE);
		assertThat(toCategory(WASTE_MANAGEMENT)).isEqualTo(Category.WASTE_MANAGEMENT);
		assertThat(toCategory(WATER)).isEqualTo(Category.WATER);
	}

	@Test
	void testToAgreementsWhenNull() {
		assertThat(toAgreements(null)).isNotNull().isEmpty();
	}

	@Test
	void testToAgreementsWhenResponseEmpty() {
		assertThat(toAgreements(createResponse(0))).isNotNull().isEmpty();
	}

	@Test
	void testToAgreements() {
		final var agreementSize = 2;
		final var agreements = toAgreements(createResponse(agreementSize));

		assertThat(agreements).isNotNull().hasSize(agreementSize);
		for (int i = 0; i < agreementSize; i++) {
			assertAgreementValues(agreements.get(i), i, false, true, (i % 2) == 0, now().plusYears(i));
			assertThat(agreements.get(i).getCustomerId()).isEqualTo(CUSTOMER_ID + "0");
		}
	}

	@Test
	void testToAgreementPartiesWhenNull() {
		assertThat(toAgreementParties(null)).isNotNull().isEmpty();
	}

	@Test
	void testToAgreementPartiesWhenResponseEmpty() {
		assertThat(toAgreementParties(createResponse(0))).isNotNull().isEmpty();
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2 })
	void testToAgreementPartiesAllAgreementsBelongToSameParty(int agreementSize) {
		final var agreementParties = toAgreementParties(createResponse(agreementSize));
		assertThat(agreementParties).isNotNull().hasSize(1);

		final var agreementParty = agreementParties.get(0);
		assertThat(agreementParty.getCustomerId()).isEqualTo(CUSTOMER_ID + "0");

		assertThat(agreementParty).isNotNull().extracting(AgreementParty::getAgreements).asInstanceOf(LIST).hasSize(agreementSize);
		for (int i = 0; i < agreementSize; i++) {
			assertAgreementValues(agreementParty.getAgreements().get(i), i, false, true, (i % 2) == 0, now().plusYears(i));
		}
	}

	@Test
	void testToAgreementPartiesAgreementBelongToDifferentParties() {
		final var agreementParties = toAgreementParties(createResponse(2, false, true, false));
		assertThat(agreementParties).isNotNull().hasSize(2);

		for (int i = 0; i < 2; i++) {
			final var agreementParty = agreementParties.get(i);
			assertThat(agreementParty.getCustomerId()).isEqualTo(CUSTOMER_ID + i);

			assertThat(agreementParty).extracting(AgreementParty::getAgreements).asInstanceOf(LIST).hasSize(1);
			assertAgreementValues(agreementParty.getAgreements().get(0), i, true, false, (i % 2) == 0, now().plusYears(i));
		}
	}

	@Test
	void testToAgreementPartiesAgreementsForNonActiveAgreements() {
		final var dataWarehouseReaderResponse = createResponse(2, false, true, false);
		dataWarehouseReaderResponse.getAgreements().stream().forEach(agreement -> agreement.setToDate(now().minusDays(1)));

		final var agreementParties = toAgreementParties(dataWarehouseReaderResponse);
		assertThat(agreementParties).isNotNull().hasSize(2);

		for (int i = 0; i < 2; i++) {
			final var agreementParty = agreementParties.get(i);
			assertThat(agreementParty.getCustomerId()).isEqualTo(CUSTOMER_ID + i);

			assertThat(agreementParty).extracting(AgreementParty::getAgreements).asInstanceOf(LIST).hasSize(1);
			assertAgreementValues(agreementParty.getAgreements().get(0), i, true, false, (i % 2) == 0, now().minusDays(1));
		}
	}

	@Test
	void testToAgreementPartiesAgreementsForAgreementsWithNoEndDate() {
		final var dataWarehouseReaderResponse = createResponse(1);
		dataWarehouseReaderResponse.getAgreements().stream().forEach(agreement -> agreement.setToDate(null));

		final var agreementParties = toAgreementParties(dataWarehouseReaderResponse);

		final var agreementParty = agreementParties.get(0);
		assertThat(agreementParty.getCustomerId()).isEqualTo(CUSTOMER_ID + "0");

		assertThat(agreementParty).isNotNull().extracting(AgreementParty::getAgreements).asInstanceOf(LIST).hasSize(1);
		assertAgreementValues(agreementParty.getAgreements().get(0), 0, false, true, true, null);
	}

	private void assertAgreementValues(se.sundsvall.agreement.api.model.Agreement agreement, int i, boolean bound, boolean mainAgreement, boolean active, LocalDate toDate) {
		assertThat(agreement.isActive()).isEqualTo(active);
		assertThat(agreement.getAgreementId()).isEqualTo(AGREEMENT_ID + i);
		assertThat(agreement.getBillingId()).isEqualTo(BILLING_ID + i);
		assertThat(agreement.isBinding()).isEqualTo(bound);
		assertThat(agreement.getBindingRule()).isEqualTo(bound ? BINDING_RULE : null);
		assertThat(agreement.getDescription()).isEqualTo(DESCRIPTION + i);
		assertThat(agreement.getFromDate()).isEqualTo(now().minusYears(i));
		assertThat(agreement.isMainAgreement()).isEqualTo(mainAgreement);
		assertThat(agreement.getToDate()).isEqualTo(toDate);
	}

	@ParameterizedTest
	@EnumSource(se.sundsvall.agreement.api.model.Category.class)
	void testMapFromDataWarehouseReaderCategories(se.sundsvall.agreement.api.model.Category category) {
		final var dataWarehouseReaderResponse = createResponse(1);
		dataWarehouseReaderResponse.getAgreements().get(0).setCategory(toCategory(category));

		final var agreementParties = toAgreementParties(dataWarehouseReaderResponse);
		assertThat(agreementParties.get(0).getAgreements().get(0).getCategory()).isEqualTo(category);
	}

	/**
	 * Utility method returning dataWarehouseReader response with list of size x agreements belonging to same customer where
	 * binding is false and mainAgreement is true
	 */
	private static generated.se.sundsvall.datawarehousereader.AgreementResponse createResponse(int size) {
		return createResponse(size, true, false, true);
	}

	/**
	 * Utility method returning dataWarehouseReader response with list of size x agreements with different data depending on
	 * incoming settings
	 */
	private static generated.se.sundsvall.datawarehousereader.AgreementResponse createResponse(int size, boolean sameCustomer, boolean binding, boolean mainAgreement) {
		final PagingAndSortingMetaData meta = new PagingAndSortingMetaData().totalRecords(Long.valueOf(size));
		final generated.se.sundsvall.datawarehousereader.AgreementResponse response = new generated.se.sundsvall.datawarehousereader.AgreementResponse();
		response.meta(meta);

		for (int i = 0; i < size; i++) {
			response.addAgreementsItem(new Agreement()
				.agreementId(AGREEMENT_ID + i)
				.billingId(BILLING_ID + i)
				.binding(binding)
				.bindingRule(binding ? BINDING_RULE : null)
				.category(Category.COMMUNICATION)
				.customerNumber(CUSTOMER_ID + (sameCustomer ? 0 : i))
				.description(DESCRIPTION + i)
				.facilityId(FACILITY_ID)
				.fromDate(now().minusYears(i))
				.mainAgreement(mainAgreement)
				.partyId(PARTY_ID + (sameCustomer ? 0 : i))
				.toDate(now().plusYears(i))
				.active((i % 2) == 0));
		}

		return response;
	}
}
