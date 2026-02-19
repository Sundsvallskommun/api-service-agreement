package se.sundsvall.agreement.api.model;

import java.util.List;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class AgreementResponseTest {

	@Test
	void testBean() {
		assertThat(AgreementResponse.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var agreementParties = List.of(AgreementParty.create());

		final var agreementResponse = AgreementResponse.create()
			.withAgreementParties(agreementParties);

		assertThat(agreementResponse).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(agreementResponse.getAgreementParties()).isEqualTo(agreementParties);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(AgreementResponse.create()).hasAllNullFieldsOrProperties();
		assertThat(new AgreementResponse()).hasAllNullFieldsOrProperties();
	}
}
