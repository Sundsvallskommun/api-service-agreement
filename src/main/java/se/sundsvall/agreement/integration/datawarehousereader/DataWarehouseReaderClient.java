package se.sundsvall.agreement.integration.datawarehousereader;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static se.sundsvall.agreement.integration.datawarehousereader.configuration.DataWarehouseReaderConfiguration.CLIENT_REGISTRATION_ID;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import generated.se.sundsvall.datawarehousereader.AgreementResponse;
import generated.se.sundsvall.datawarehousereader.Category;
import se.sundsvall.agreement.integration.datawarehousereader.configuration.DataWarehouseReaderConfiguration;

@FeignClient(name = CLIENT_REGISTRATION_ID, url = "${integration.datawarehousereader.url}", configuration = DataWarehouseReaderConfiguration.class)
public interface DataWarehouseReaderClient {

	@GetMapping(path = "agreements", produces = APPLICATION_JSON_VALUE)
	AgreementResponse getAgreementsByCategoryAndFacility(
		@RequestParam("category") Category category,
		@RequestParam("facilityId") String facilityId,
		@RequestParam("page") int page,
		@RequestParam("limit") int limit,
		@RequestParam("active") Boolean active);

	@GetMapping(path = "agreements", produces = APPLICATION_JSON_VALUE)
	AgreementResponse getAgreementsByPartyIdAndCategories(
		@RequestParam("partyId") String partyId,
		@RequestParam("category") List<Category> categories,
		@RequestParam("page") int page,
		@RequestParam("limit") int limit,
		@RequestParam("active") Boolean active);
}
