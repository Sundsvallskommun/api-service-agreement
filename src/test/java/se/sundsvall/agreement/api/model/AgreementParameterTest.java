package se.sundsvall.agreement.api.model;

import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class AgreementParameterTest {

	@Test
	void testBean() {
		assertThat(AgreementParameters.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var onlyActive = true;
		final var page = 12;
		final var limit = 34;

		var parameters = AgreementParameters.create()
			.withOnlyActive(onlyActive)
			.withPage(page)
			.withLimit(limit);

		assertThat(parameters.isOnlyActive()).isTrue();
		assertThat(parameters.getPage()).isEqualTo(page);
		assertThat(parameters.getLimit()).isEqualTo(limit);
	}
}
