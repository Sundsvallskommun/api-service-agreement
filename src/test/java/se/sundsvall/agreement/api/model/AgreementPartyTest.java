package se.sundsvall.agreement.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class AgreementPartyTest {

	@Test
	void testBean() {
		assertThat(AgreementParty.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var agreements = List.of(Agreement.create());
		final var customerId = "customerId";
		
		final var agreementParty = AgreementParty.create()
			.withAgreements(agreements)
			.withCustomerId(customerId);
		
		assertThat(agreementParty).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(agreementParty.getAgreements()).isEqualTo(agreements);
		assertThat(agreementParty.getCustomerId()).isEqualTo(customerId);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(AgreementParty.create()).hasAllNullFieldsOrProperties();
		assertThat(new AgreementParty()).hasAllNullFieldsOrProperties();
	}
}
