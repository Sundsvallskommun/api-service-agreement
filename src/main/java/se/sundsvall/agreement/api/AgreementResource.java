package se.sundsvall.agreement.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import se.sundsvall.agreement.api.model.AgreementResponse;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.service.AgreementService;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;

@RestController
@Validated
@RequestMapping("/agreements")
@Tag(name = "Agreement", description = "Agreement resources")
public class AgreementResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(AgreementResource.class);

	@Autowired
	private AgreementService agreementService;

	@GetMapping(path = "/{category}/{facilityId}", produces = { APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Get agreements by category and facility-id")
	@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AgreementResponse.class)))
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = { Problem.class, ConstraintViolationProblem.class })))
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<AgreementResponse> getAgreementsByCategoryAndFacilityId(
		@Parameter(name = "category", description = "Agreement category", example = "ELECTRICITY") @PathVariable(name = "category") Category category,
		@Parameter(name = "facilityId", description = "Id for the facility", example = "1471222") @PathVariable(name = "facilityId") String facilityId,
		@Parameter(name = "onlyActive", description = "Signal if only active or all agreements should be included in response, default is to only return active agreements.", example = "true") @RequestParam(name = "onlyActive", defaultValue = "true") boolean onlyActive) {

		LOGGER.debug("Received request to getAgreementsByCategoryAndFacilityId(): category='{}', facilityId='{}', , onlyActive='{}'", category, facilityId, onlyActive);

		return ResponseEntity.ok(agreementService.getAgreementsByCategoryAndFacilityId(category, facilityId, onlyActive));
	}

	@GetMapping(path = "/{partyId}", produces = { APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Get agreements connected to a party-ID, optionally filtered by provided categories")
	@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AgreementResponse.class)))
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = { Problem.class, ConstraintViolationProblem.class })))
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<AgreementResponse> getAgreementsForPartyId(
		@Parameter(name = "partyId", description = "Party-ID", required = true, example = "81471222-5798-11e9-ae24-57fa13b361e1") @ValidUuid @PathVariable(name = "partyId") String partyId,
		@Parameter(name = "category", description = "Optional list of one or more agreement categories to be included in response, default is to return all agreements connected to the party-ID") @RequestParam(value = "category", defaultValue = "") List<Category> categories,
		@Parameter(name = "onlyActive", description = "Signal if only active or all agreements should be included in response, default is to only return active agreements.", example = "true") @RequestParam(name = "onlyActive", defaultValue = "true") boolean onlyActive) {

		LOGGER.debug("Received request to getAgreementsForPartyId(): partyId='{}', categories='{}'", partyId, categories);

		return ResponseEntity.ok(agreementService.getAgreementsByPartyIdAndCategories(partyId, categories, onlyActive));
	}
}
