package se.sundsvall.agreement.api.model;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import se.sundsvall.dept44.models.api.paging.PagingMetaData;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class PagedAgreementResponseTest {

	@Test
	void testBean() {
		assertThat(PagedAgreementResponse.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var agreements = new ArrayList<Agreement>();
		final var meta = PagingMetaData.create();

		final var result = PagedAgreementResponse.create()
			.withAgreements(agreements)
			.withMetaData(meta);

		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getAgreements()).isSameAs(agreements);
		assertThat(result.getMetaData()).isSameAs(meta);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(PagedAgreementResponse.create()).hasAllNullFieldsOrProperties();
	}
}
