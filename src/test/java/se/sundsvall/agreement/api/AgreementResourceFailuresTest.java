package se.sundsvall.agreement.api;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import se.sundsvall.agreement.Application;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.service.AgreementService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class AgreementResourceFailuresTest {

	@MockBean
	private AgreementService agreementServiceMock;

	@Autowired
	private WebTestClient webTestClient;
	
	@Test
	void getAgreementsByInvalidCategory() {

		// Parameter values
		final var id = "1234567";

		webTestClient.get().uri("/agreements/{category}/{facilityId}", "INVALID-CATEGORY", id)
		.exchange()
		.expectStatus().isBadRequest()
		.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
		.expectBody()
		.jsonPath("$.title").isEqualTo("Bad Request")
		.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
		.jsonPath("$.detail").isEqualTo(
				"""
				Failed to convert value of type 'java.lang.String' to required type 'se.sundsvall.agreement.api.model.Category'; nested exception is org.springframework.core.convert.ConversionFailedException: \
				Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.Parameter @org.springframework.web.bind.annotation.PathVariable se.sundsvall.agreement.api.model.Category] \
				for value 'INVALID-CATEGORY'; nested exception is java.lang.IllegalArgumentException: No enum constant se.sundsvall.agreement.api.model.Category.INVALID-CATEGORY""");

		verifyNoInteractions(agreementServiceMock);
	}

	@Test
	void getAgreementsWrongmethod() {

		// Parameter values
		final var id = "1234567";

		webTestClient.delete().uri("/agreements/{category}/{facilityId}", Category.ELECTRICITY, id)
		.exchange()
		.expectStatus().isEqualTo(METHOD_NOT_ALLOWED)
		.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
		.expectBody()
		.jsonPath("$.title").isEqualTo("Method Not Allowed")
		.jsonPath("$.status").isEqualTo(METHOD_NOT_ALLOWED.value())
		.jsonPath("$.detail").isEqualTo("Request method 'DELETE' not supported");

		verifyNoInteractions(agreementServiceMock);
	}

	@Test
	void getAgreementsByInvalidPartyId() {

		webTestClient.get().uri("/agreements/invalid-party-id")
		.exchange()
		.expectStatus().isBadRequest()
		.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
		.expectBody()
		.jsonPath("$.title").isEqualTo("Constraint Violation")
		.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
		.jsonPath("$.violations[0].field").isEqualTo("getAgreementsForPartyId.partyId")
		.jsonPath("$.violations[0].message").isEqualTo("not a valid UUID");

		verifyNoInteractions(agreementServiceMock);
	}
}
