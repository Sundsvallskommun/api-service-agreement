package se.sundsvall.agreement.api;

import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.agreement.Application;
import se.sundsvall.agreement.api.model.Agreement;
import se.sundsvall.agreement.api.model.AgreementParty;
import se.sundsvall.agreement.api.model.AgreementResponse;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.service.AgreementService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class AgreementResourceTest {

	private static final String CATEGORY_AND_FACILITY_ID_PATH = "/{municipalityId}/agreements/{category}/{facilityId}";
	private static final String PARTY_ID_PATH = "/{municipalityId}/agreements/{partyId}";

	@MockitoBean
	private AgreementService agreementServiceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getAgreementsByCategoryAndFacilityIdWithOnlyActiveAbsent() {

		// Arrange
		final var municipalityId = "2281";
		final var category = Category.ELECTRICITY;
		final var facilityId = "1234567";

		when(agreementServiceMock.getAgreementsByCategoryAndFacilityId(municipalityId, category, facilityId, true))
			.thenReturn(AgreementResponse.create().withAgreementParties(List.of(AgreementParty.create().withAgreements(List.of(Agreement.create())))));

		// Act
		final var response = webTestClient.get().uri(CATEGORY_AND_FACILITY_ID_PATH, municipalityId, category, facilityId)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(AgreementResponse.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getAgreementParties()).hasSize(1);
		assertThat(response.getAgreementParties().get(0).getAgreements()).hasSize(1)
			.extracting(Agreement::isMainAgreement, Agreement::isBinding)
			.containsExactly(tuple(false, false));

		verify(agreementServiceMock).getAgreementsByCategoryAndFacilityId(municipalityId, category, facilityId, true);
	}

	@Test
	void getAgreementsByCategoryAndFacilityIdWithOnlyActiveTrue() {

		// Arrange
		final var municipalityId = "2281";
		final var category = Category.ELECTRICITY;
		final var facilityId = "1234567";
		final var onlyActive = true;

		when(agreementServiceMock.getAgreementsByCategoryAndFacilityId(municipalityId, category, facilityId, onlyActive))
			.thenReturn(AgreementResponse.create().withAgreementParties(List.of(AgreementParty.create().withAgreements(List.of(Agreement.create())))));

		// Act
		final var response = webTestClient.get().uri(builder -> builder.path(CATEGORY_AND_FACILITY_ID_PATH)
			.queryParam("onlyActive", String.valueOf(onlyActive))
			.build(municipalityId, category, facilityId))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(AgreementResponse.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getAgreementParties()).hasSize(1);
		assertThat(response.getAgreementParties().get(0).getAgreements())
			.extracting(Agreement::isMainAgreement, Agreement::isBinding)
			.containsExactly(tuple(false, false));

		verify(agreementServiceMock).getAgreementsByCategoryAndFacilityId(municipalityId, category, facilityId, onlyActive);
	}

	@Test
	void getAgreementsByCategoryAndFacilityIdWithOnlyActiveFalse() {

		// Arrange
		final var municipalityId = "2281";
		final var category = Category.ELECTRICITY;
		final var facilityId = "1234567";
		final var onlyActive = false;

		when(agreementServiceMock.getAgreementsByCategoryAndFacilityId(municipalityId, category, facilityId, onlyActive))
			.thenReturn(AgreementResponse.create().withAgreementParties(List.of(
				AgreementParty.create().withAgreements(List.of(Agreement.create(), Agreement.create())),
				AgreementParty.create().withAgreements(List.of(Agreement.create(), Agreement.create())))));

		// Act
		final var response = webTestClient.get().uri(builder -> builder.path(CATEGORY_AND_FACILITY_ID_PATH)
			.queryParam("onlyActive", String.valueOf(onlyActive))
			.build(municipalityId, category, facilityId))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(AgreementResponse.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getAgreementParties()).hasSize(2);
		assertThat(response.getAgreementParties().get(0).getAgreements())
			.extracting(Agreement::isMainAgreement, Agreement::isBinding)
			.containsExactly(
				tuple(false, false),
				tuple(false, false));
		assertThat(response.getAgreementParties().get(1).getAgreements())
			.extracting(Agreement::isMainAgreement, Agreement::isBinding)
			.containsExactly(
				tuple(false, false),
				tuple(false, false));

		verify(agreementServiceMock).getAgreementsByCategoryAndFacilityId(municipalityId, category, facilityId, onlyActive);
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesMinimumParameters() {

		// Arrange
		final var municipalityId = "2281";
		final var partyId = randomUUID().toString();

		when(agreementServiceMock.getAgreementsByPartyIdAndCategories(municipalityId, partyId, emptyList(), true))
			.thenReturn(AgreementResponse.create().withAgreementParties(List.of(AgreementParty.create().withAgreements(List.of(Agreement.create())))));

		// Act
		final var response = webTestClient.get().uri(builder -> builder.path(PARTY_ID_PATH).build(municipalityId, partyId))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(AgreementResponse.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getAgreementParties()).hasSize(1);
		assertThat(response.getAgreementParties().get(0).getAgreements())
			.extracting(Agreement::isMainAgreement, Agreement::isBinding)
			.containsExactly(tuple(false, false));

		verify(agreementServiceMock).getAgreementsByPartyIdAndCategories(municipalityId, partyId, emptyList(), true);
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesWithCategoryFilters() {

		// Arrange
		final var municipalityId = "2281";
		final var partyId = randomUUID().toString();
		final var categories = List.of(Category.DISTRICT_COOLING, Category.DISTRICT_HEATING);

		when(agreementServiceMock.getAgreementsByPartyIdAndCategories(municipalityId, partyId, categories, true))
			.thenReturn(AgreementResponse.create().withAgreementParties(List.of(AgreementParty.create().withAgreements(List.of(Agreement.create())))));

		// Act
		final var response = webTestClient.get().uri(builder -> builder.path(PARTY_ID_PATH)
			.queryParam("category", categories)
			.build(municipalityId, partyId))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(AgreementResponse.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getAgreementParties()).hasSize(1);
		assertThat(response.getAgreementParties().get(0).getAgreements())
			.extracting(Agreement::isMainAgreement, Agreement::isBinding)
			.containsExactly(tuple(false, false));

		verify(agreementServiceMock).getAgreementsByPartyIdAndCategories(municipalityId, partyId, categories, true);
	}

	@Test
	void getAgreementsByPartyIdAndCategoriesWithCategoryFiltersAndOnlyActiveFalse() {

		// Arrange
		final var municipalityId = "2281";
		final var partyId = randomUUID().toString();
		final var categories = List.of(Category.DISTRICT_COOLING, Category.DISTRICT_HEATING);
		final var onlyActive = false;

		when(agreementServiceMock.getAgreementsByPartyIdAndCategories(municipalityId, partyId, categories, onlyActive))
			.thenReturn(AgreementResponse.create().withAgreementParties(List.of(AgreementParty.create().withAgreements(List.of(Agreement.create())))));

		// Act
		final var response = webTestClient.get().uri(builder -> builder.path(PARTY_ID_PATH)
			.queryParam("category", categories)
			.queryParam("onlyActive", String.valueOf(onlyActive))
			.build(municipalityId, partyId))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(AgreementResponse.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getAgreementParties()).hasSize(1);
		assertThat(response.getAgreementParties().get(0).getAgreements())
			.extracting(Agreement::isMainAgreement, Agreement::isBinding)
			.containsExactly(tuple(false, false));

		verify(agreementServiceMock).getAgreementsByPartyIdAndCategories(municipalityId, partyId, categories, onlyActive);
	}
}
