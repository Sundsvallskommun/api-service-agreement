package se.sundsvall.agreement.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.agreement.Application;
import se.sundsvall.agreement.api.model.Agreement;
import se.sundsvall.agreement.api.model.AgreementParameters;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.api.model.PagedAgreementResponse;
import se.sundsvall.agreement.service.AgreementService;
import se.sundsvall.dept44.models.api.paging.PagingMetaData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class PagedAgreementResourceTest {

	@MockBean
	private AgreementService agreementServiceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getAgreementsForPartyIdDefaultValues() {
		// Arrange
		final var partyId = UUID.randomUUID().toString();
		final var categories = new ArrayList<Category>();
		final var parameters = new AgreementParameters();

		final var pagingResponse = PagedAgreementResponse.create()
			.withMetaData(PagingMetaData.create()
				.withPage(1)
				.withLimit(100)
				.withCount(1)
				.withTotalRecords(1)
				.withTotalPages(1))
			.withAgreements(List.of(Agreement.create().withAgreementId("agreementId")));
		when(agreementServiceMock.getPagedAgreementsByPartyIdAndCategories(any(), any(), any()))
			.thenReturn(pagingResponse);

		// Act
		final var response = webTestClient.get().uri(builder -> builder.path("paged/agreements/{partyId}")
				.build(partyId))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(PagedAgreementResponse.class)
			.returnResult()
			.getResponseBody();

		verify(agreementServiceMock).getPagedAgreementsByPartyIdAndCategories(partyId, categories, parameters);
		assertThat(response.getMetaData()).isEqualTo(pagingResponse.getMetaData());
		assertThat(response.getAgreements()).isEqualTo(pagingResponse.getAgreements());
	}


	@Test
	void getAgreementsForPartyIdNonDefaultValues() {
		// Arrange
		final var partyId = UUID.randomUUID().toString();
		final var categories = List.of(Category.DISTRICT_COOLING, Category.DISTRICT_HEATING);
		final var parameters = AgreementParameters.create()
				.withOnlyActive(false)
				.withPage(2)
				.withLimit(123);

		final var pagingResponse = PagedAgreementResponse.create()
			.withMetaData(PagingMetaData.create()
				.withPage(2)
				.withLimit(123)
				.withCount(33)
				.withTotalRecords(444)
				.withTotalPages(9))
			.withAgreements(List.of(Agreement.create().withAgreementId("agreementId")));
		when(agreementServiceMock.getPagedAgreementsByPartyIdAndCategories(any(), any(), any()))
			.thenReturn(pagingResponse);

		// Act
		final var response = webTestClient.get().uri(builder -> builder.path("paged/agreements/{partyId}")
				.queryParam("category", categories)
				.queryParam("onlyActive", parameters.isOnlyActive())
				.queryParam("limit", parameters.getLimit())
				.queryParam("page", parameters.getPage())
				.build(partyId))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(PagedAgreementResponse.class)
			.returnResult()
			.getResponseBody();

		verify(agreementServiceMock).getPagedAgreementsByPartyIdAndCategories(partyId, categories, parameters);
		assertThat(response.getMetaData()).isEqualTo(pagingResponse.getMetaData());
		assertThat(response.getAgreements()).isEqualTo(pagingResponse.getAgreements());
	}
}