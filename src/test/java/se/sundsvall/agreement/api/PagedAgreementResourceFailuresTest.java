package se.sundsvall.agreement.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.agreement.Application;
import se.sundsvall.agreement.service.AgreementService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.zalando.problem.Status.BAD_REQUEST;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class PagedAgreementResourceFailuresTest {

	@MockBean
	private AgreementService agreementServiceMock;

	@Autowired
	private WebTestClient webTestClient;


	@Test
	void getAgreementsByInvalidPartyId() {
		// Act
		final var response = webTestClient.get().uri("paged/agreements/invalid-party-id")
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getAgreementsForPartyId.partyId", "not a valid UUID"));

		verifyNoInteractions(agreementServiceMock);
	}

	@Test
	void getAgreementsByInvalidPaging() {
		// Act
		final var response = webTestClient.get().uri(builder -> builder.path("paged/agreements/{partyId}")
				.queryParam("limit", 0)
				.queryParam("page", 0)
				.build(UUID.randomUUID().toString()))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(
				tuple("limit", "must be greater than or equal to 1"),
				tuple("page", "must be greater than or equal to 1"));

		verifyNoInteractions(agreementServiceMock);
	}
}