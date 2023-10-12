package se.sundsvall.agreement.api.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Random;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.LocalDate.MAX;
import static java.time.LocalDate.MIN;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class AgreementTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(Agreement.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var customerId = "customerId";
		final var active = true;
		final var agreementId = "agreementId";
		final var billingId = "billingId";
		final var category = Category.DISTRICT_COOLING;
		final var description = "description";
		final var facilityId = "facilityId";
		final var mainAgreement = true;
		final var binding = true;
		final var bindingRule = "bindingRule";
		final var fromDate = MIN;
		final var toDate = MAX;

		final var agreement = Agreement.create()
			.withCustomerId(customerId)
			.withActive(active)
			.withAgreementId(agreementId)
			.withBillingId(billingId)
			.withCategory(category)
			.withDescription(description)
			.withFacilityId(facilityId)
			.withMainAgreement(mainAgreement)
			.withBinding(binding)
			.withBindingRule(bindingRule)
			.withFromDate(fromDate)
			.withToDate(toDate);

		assertThat(agreement).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(agreement.getCustomerId()).isEqualTo(customerId);
		assertThat(agreement.isActive()).isEqualTo(active);
		assertThat(agreement.getAgreementId()).isEqualTo(agreementId);
		assertThat(agreement.getBillingId()).isEqualTo(billingId);
		assertThat(agreement.getCategory()).isEqualTo(category);
		assertThat(agreement.getDescription()).isEqualTo(description);
		assertThat(agreement.getFacilityId()).isEqualTo(facilityId);
		assertThat(agreement.isMainAgreement()).isEqualTo(mainAgreement);
		assertThat(agreement.isBinding()).isEqualTo(binding);
		assertThat(agreement.getBindingRule()).isEqualTo(bindingRule);
		assertThat(agreement.getFromDate()).isEqualTo(fromDate);
		assertThat(agreement.getToDate()).isEqualTo(toDate);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Agreement.create()).hasAllNullFieldsOrPropertiesExcept("active", "binding", "mainAgreement")
			.hasFieldOrPropertyWithValue("active", false)
			.hasFieldOrPropertyWithValue("binding", false)
			.hasFieldOrPropertyWithValue("mainAgreement", false);
	}
}
