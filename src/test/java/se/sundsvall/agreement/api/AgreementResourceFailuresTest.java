package se.sundsvall.agreement.api;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.zalando.problem.Status.BAD_REQUEST;
import static org.zalando.problem.Status.METHOD_NOT_ALLOWED;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;

import se.sundsvall.agreement.Application;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.service.AgreementService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class AgreementResourceFailuresTest {

	private static final String CATEGORY_AND_FACILITY_ID_PATH = "/{municipalityId}/agreements/{category}/{facilityId}";
	private static final String PARTY_ID_PATH = "/{municipalityId}/agreements/{partyId}";

	@MockBean
	private AgreementService agreementServiceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getAgreementsByInvalidCategory() {

		// Arrange
		final var municipalityId = "2281";
		final var category = "INVALID-CATEGORY";
		final var id = "1234567";

		// Act
		final var response = webTestClient.get().uri(CATEGORY_AND_FACILITY_ID_PATH, municipalityId, category, id)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo(BAD_REQUEST.getReasonPhrase());
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo(
			"Failed to convert value of type 'java.lang.String' to required type 'se.sundsvall.agreement.api.model.Category'; Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.Parameter @org.springframework.web.bind.annotation.PathVariable se.sundsvall.agreement.api.model.Category] for value [INVALID-CATEGORY]");

		verifyNoInteractions(agreementServiceMock);
	}

	@Test
	void getAgreementsByCategoryAndFacilityIdInvalidMunicipalityId() {

		// Arrange
		final var municipalityId = "invalid";
		final var category = Category.ELECTRICITY;
		final var id = "1234567";

		// Act
		final var response = webTestClient.get().uri(CATEGORY_AND_FACILITY_ID_PATH, municipalityId, category, id)
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
			.containsExactly(tuple("getAgreementsByCategoryAndFacilityId.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(agreementServiceMock);
	}

	@Test
	void methodNotSupported() {

		// Arrange
		final var municipalityId = "2281";
		final var category = Category.ELECTRICITY;
		final var id = "1234567";

		// Act
		final var response = webTestClient.delete().uri(CATEGORY_AND_FACILITY_ID_PATH, municipalityId, category, id)
			.exchange()
			.expectStatus().isEqualTo(METHOD_NOT_ALLOWED.getStatusCode())
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo(METHOD_NOT_ALLOWED.getReasonPhrase());
		assertThat(response.getStatus()).isEqualTo(METHOD_NOT_ALLOWED);
		assertThat(response.getDetail()).isEqualTo("Request method 'DELETE' is not supported");

		verifyNoInteractions(agreementServiceMock);
	}

	@Test
	void getAgreementsByInvalidPartyId() {

		// Arrange
		final var municipalityId = "2281";
		final var partyId = "invalid";

		// Act
		final var response = webTestClient.get().uri(PARTY_ID_PATH, municipalityId, partyId)
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
	void getAgreementsByPartyIdInvalidMunicipalityId() {

		// Arrange
		final var municipalityId = "invalid";
		final var partyId = randomUUID().toString();

		// Act
		final var response = webTestClient.get().uri(PARTY_ID_PATH, municipalityId, partyId)
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
			.containsExactly(tuple("getAgreementsForPartyId.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(agreementServiceMock);
	}
}
