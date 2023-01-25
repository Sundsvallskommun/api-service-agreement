package se.sundsvall.agreement.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import se.sundsvall.agreement.Application;
import se.sundsvall.agreement.api.model.Agreement;
import se.sundsvall.agreement.api.model.AgreementParty;
import se.sundsvall.agreement.api.model.AgreementResponse;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.service.AgreementService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class AgreementResourceTest {

	@MockBean
	private AgreementService agreementServiceMock;

	@Autowired
	private WebTestClient webTestClient;
	
	@Test
	void getAgreementsByCategoryAndFacilityIdWithOnlyActiveAbsent() {

		// Parameter values
		final var category = Category.ELECTRICITY;
		final var facilityId = "1234567";
		
		when(agreementServiceMock.getAgreementsByCategoryAndFacilityId(category, facilityId, true))
		.thenReturn(AgreementResponse.create().withAgreementParties(List.of(AgreementParty.create().withAgreements(List.of(Agreement.create())))));

		webTestClient.get().uri("/agreements/{category}/{facilityId}", category, facilityId)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.agreementParties").isArray()
		.jsonPath("$.agreementParties[0].agreements").isArray()
		.jsonPath("$.agreementParties[0].agreements[0].mainAgreement").isEqualTo("false")
		.jsonPath("$.agreementParties[0].agreements[0].binding").isEqualTo("false");
		
		verify(agreementServiceMock).getAgreementsByCategoryAndFacilityId(category, facilityId, true);
	}

	@Test
	void getAgreementsByCategoryAndFacilityIdWithOnlyActiveTrue() {

		// Parameter values
		final var category = Category.ELECTRICITY;
		final var facilityId = "1234567";
		final var onlyActive = true;
		
		when(agreementServiceMock.getAgreementsByCategoryAndFacilityId(category, facilityId, onlyActive))
		.thenReturn(AgreementResponse.create().withAgreementParties(List.of(AgreementParty.create().withAgreements(List.of(Agreement.create())))));

		webTestClient.get().uri(builder -> builder.path("/agreements/{category}/{facilityId}").queryParam("onlyActive", String.valueOf(onlyActive)).build(category, facilityId))
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.agreementParties").isArray()
		.jsonPath("$.agreementParties[0].agreements").isArray()
		.jsonPath("$.agreementParties[0].agreements[0].mainAgreement").isEqualTo("false")
		.jsonPath("$.agreementParties[0].agreements[0].binding").isEqualTo("false");
		
		verify(agreementServiceMock).getAgreementsByCategoryAndFacilityId(category, facilityId, onlyActive);
	}

	@Test
	void getAgreementsByCategoryAndFacilityIdWithOnlyActiveFalse() {

		// Parameter values
		final var category = Category.ELECTRICITY;
		final var facilityId = "1234567";
		final var onlyActive = false;
		
		when(agreementServiceMock.getAgreementsByCategoryAndFacilityId(category, facilityId, onlyActive))
		.thenReturn(AgreementResponse.create().withAgreementParties(List.of(
			AgreementParty.create().withAgreements(List.of(Agreement.create(), Agreement.create())), 
			AgreementParty.create().withAgreements(List.of(Agreement.create(), Agreement.create())))
		));

		webTestClient.get().uri(builder -> builder.path("/agreements/{category}/{facilityId}").queryParam("onlyActive", String.valueOf(onlyActive)).build(category, facilityId))
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.agreementParties").isArray()
		.jsonPath("$.agreementParties[0].agreements").isArray()
		.jsonPath("$.agreementParties[0].agreements[0].mainAgreement").isEqualTo("false")
		.jsonPath("$.agreementParties[0].agreements[0].binding").isEqualTo("false")
		.jsonPath("$.agreementParties[0].agreements[1].mainAgreement").isEqualTo("false")
		.jsonPath("$.agreementParties[0].agreements[1].binding").isEqualTo("false")
		.jsonPath("$.agreementParties[1].agreements").isArray()
		.jsonPath("$.agreementParties[1].agreements[0].mainAgreement").isEqualTo("false")
		.jsonPath("$.agreementParties[1].agreements[0].binding").isEqualTo("false")
		.jsonPath("$.agreementParties[1].agreements[1].mainAgreement").isEqualTo("false")
		.jsonPath("$.agreementParties[1].agreements[1].binding").isEqualTo("false");
		
		verify(agreementServiceMock).getAgreementsByCategoryAndFacilityId(category, facilityId, onlyActive);
	}
	
	@Test
	void getAgreementsByPartyIdAndCategoriesMinimumParameters() {

		// Parameter values
		final var partyId = UUID.randomUUID().toString();

		when(agreementServiceMock.getAgreementsByPartyIdAndCategories(partyId, Collections.emptyList(), true))
		.thenReturn(AgreementResponse.create().withAgreementParties(List.of(AgreementParty.create().withAgreements(List.of(Agreement.create())))));

		webTestClient.get().uri(builder -> builder.path("/agreements/{partyId}").build(partyId))
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.agreementParties").isArray()
		.jsonPath("$.agreementParties[0].agreements").isArray()
		.jsonPath("$.agreementParties[0].agreements[0].mainAgreement").isEqualTo("false")
		.jsonPath("$.agreementParties[0].agreements[0].binding").isEqualTo("false");
		
		verify(agreementServiceMock).getAgreementsByPartyIdAndCategories(partyId, Collections.emptyList(), true);
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesWithCategoryFilters() {
		// Parameter values
		final var partyId = UUID.randomUUID().toString();
		final var categories = List.of(Category.DISTRICT_COOLING, Category.DISTRICT_HEATING);

		when(agreementServiceMock.getAgreementsByPartyIdAndCategories(partyId, categories, true))
		.thenReturn(AgreementResponse.create().withAgreementParties(List.of(AgreementParty.create().withAgreements(List.of(Agreement.create())))));
		
		webTestClient.get().uri(builder -> builder.path("/agreements/{partyId}").queryParam("category", categories).build(partyId))
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.agreementParties").isArray()
		.jsonPath("$.agreementParties[0].agreements").isArray()
		.jsonPath("$.agreementParties[0].agreements[0].mainAgreement").isEqualTo("false")
		.jsonPath("$.agreementParties[0].agreements[0].binding").isEqualTo("false");
		
		verify(agreementServiceMock).getAgreementsByPartyIdAndCategories(partyId, categories, true);
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesWithCategoryFiltersAndOnlyActiveFalse() {
		
		// Parameter values
		final var partyId = UUID.randomUUID().toString();
		final var categories = List.of(Category.DISTRICT_COOLING, Category.DISTRICT_HEATING);
		final var onlyActive = false;

		when(agreementServiceMock.getAgreementsByPartyIdAndCategories(partyId, categories, onlyActive))
		.thenReturn(AgreementResponse.create().withAgreementParties(List.of(AgreementParty.create().withAgreements(List.of(Agreement.create())))));
		
		webTestClient.get().uri(builder -> builder.path("/agreements/{partyId}")
			.queryParam("category", categories)
			.queryParam("onlyActive", String.valueOf(onlyActive))
		.build(partyId))
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.agreementParties").isArray()
		.jsonPath("$.agreementParties[0].agreements").isArray()
		.jsonPath("$.agreementParties[0].agreements[0].mainAgreement").isEqualTo("false")
		.jsonPath("$.agreementParties[0].agreements[0].binding").isEqualTo("false");
		
		verify(agreementServiceMock).getAgreementsByPartyIdAndCategories(partyId, categories, onlyActive);
	}
}
