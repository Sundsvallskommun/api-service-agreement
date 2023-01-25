package se.sundsvall.agreement.integration.datawarehousereader;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static se.sundsvall.agreement.integration.datawarehousereader.configuration.DataWarehouseReaderConfiguration.CLIENT_REGISTRATION_ID;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import generated.se.sundsvall.datawarehousereader.AgreementResponse;
import generated.se.sundsvall.datawarehousereader.Category;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.agreement.integration.datawarehousereader.configuration.DataWarehouseReaderConfiguration;

@FeignClient(name = CLIENT_REGISTRATION_ID, url = "${integration.datawarehousereader.url}", configuration = DataWarehouseReaderConfiguration.class)
@CircuitBreaker(name = CLIENT_REGISTRATION_ID)
public interface DataWarehouseReaderClient {

	@GetMapping(path = "agreements", produces = APPLICATION_JSON_VALUE)
	AgreementResponse getAgreementsByCategoryAndFacility(@RequestParam(value = "category") Category category, @RequestParam(value = "facilityId") String facilityId);

	@GetMapping(path = "agreements", produces = APPLICATION_JSON_VALUE)
	AgreementResponse getAgreementsByPartyIdAndCategories(@RequestParam(value = "partyId") String partyId, @RequestParam(value = "category") List<Category> categories);
}
