package se.sundsvall.agreement.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.agreement.Application;
import se.sundsvall.agreement.service.AgreementService;
import se.sundsvall.dept44.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.problem.violations.Violation;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
@AutoConfigureWebTestClient
class PagedAgreementResourceFailuresTest {

	private static final String PATH = "/{municipalityId}/paged/agreements/{partyId}";

	@MockitoBean
	private AgreementService agreementServiceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getAgreementsByPartyIdInvalidPartyId() {

		// Arrange
		final var municipalityId = "2281";
		final var partyId = "invalid";

		// Act
		final var response = webTestClient.get().uri(PATH, municipalityId, partyId)
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
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("getAgreementsForPartyId.partyId", "not a valid UUID"));

		verifyNoInteractions(agreementServiceMock);
	}

	@Test
	void getAgreementsByPartyIdInvalidMunicipalityId() {

		// Arrange
		final var municipalityId = "invalid";
		final var partyId = randomUUID().toString();

		// Act
		final var response = webTestClient.get().uri(PATH, municipalityId, partyId)
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
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("getAgreementsForPartyId.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(agreementServiceMock);
	}

	@Test
	void getAgreementsByInvalidPaging() {

		// Arrange
		final var municipalityId = "2281";
		final var partyId = randomUUID().toString();

		// Act
		final var response = webTestClient.get().uri(builder -> builder.path(PATH)
			.queryParam("limit", 0)
			.queryParam("page", 0)
			.build(municipalityId, partyId))
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
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(
				tuple("limit", "must be greater than or equal to 1"),
				tuple("page", "must be greater than or equal to 1"));

		verifyNoInteractions(agreementServiceMock);
	}
}
