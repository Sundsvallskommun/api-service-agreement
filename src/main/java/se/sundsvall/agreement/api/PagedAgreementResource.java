package se.sundsvall.agreement.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.agreement.api.model.AgreementParameters;
import se.sundsvall.agreement.api.model.Category;
import se.sundsvall.agreement.api.model.PagedAgreementResponse;
import se.sundsvall.agreement.service.AgreementService;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;

@RestController
@Validated
@RequestMapping("/{municipalityId}/paged/agreements")
@Tag(name = "Paged Agreement", description = "Agreement resources with paging")
public class PagedAgreementResource {

	private final AgreementService agreementService;

	public PagedAgreementResource(final AgreementService agreementService) {
		this.agreementService = agreementService;
	}

	@GetMapping(path = "/{partyId}", produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Get agreements connected to a party-ID, optionally filtered by provided categories", responses = {
		@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
			Problem.class, ConstraintViolationProblem.class
		}))),
		@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class))),
		@ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	public ResponseEntity<PagedAgreementResponse> getAgreementsForPartyId(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "partyId", description = "Party-ID", example = "81471222-5798-11e9-ae24-57fa13b361e1") @ValidUuid @PathVariable(name = "partyId") final String partyId,
		@Parameter(name = "category", description = "Optional list of one or more agreement categories to be included in response, default is to return all agreements connected to the party-ID") @RequestParam(value = "category",
			defaultValue = "") final List<Category> categories,
		@Valid final AgreementParameters parameters) {

		return ok(agreementService.getPagedAgreementsByPartyIdAndCategories(municipalityId, partyId, categories, parameters));
	}
}
