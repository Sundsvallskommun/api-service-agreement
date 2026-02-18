package se.sundsvall.agreement.integration.datawarehousereader;

import generated.se.sundsvall.datawarehousereader.AgreementResponse;
import generated.se.sundsvall.datawarehousereader.Category;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import se.sundsvall.agreement.integration.datawarehousereader.configuration.DataWarehouseReaderConfiguration;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static se.sundsvall.agreement.integration.datawarehousereader.configuration.DataWarehouseReaderConfiguration.CLIENT_ID;

@FeignClient(name = CLIENT_ID, url = "${integration.datawarehousereader.url}", configuration = DataWarehouseReaderConfiguration.class)
@CircuitBreaker(name = CLIENT_ID)
public interface DataWarehouseReaderClient {

	@GetMapping(path = "/{municipalityId}/agreements", produces = APPLICATION_JSON_VALUE)
	AgreementResponse getAgreementsByCategoryAndFacility(
		@PathVariable("municipalityId") String municipalityId,
		@RequestParam("category") Category category,
		@RequestParam("facilityId") String facilityId,
		@RequestParam("page") int page,
		@RequestParam("limit") int limit,
		@RequestParam("active") Boolean active);

	@GetMapping(path = "/{municipalityId}/agreements", produces = APPLICATION_JSON_VALUE)
	AgreementResponse getAgreementsByPartyIdAndCategories(
		@PathVariable("municipalityId") String municipalityId,
		@RequestParam("partyId") String partyId,
		@RequestParam("category") List<Category> categories,
		@RequestParam("page") int page,
		@RequestParam("limit") int limit,
		@RequestParam("active") Boolean active);
}
