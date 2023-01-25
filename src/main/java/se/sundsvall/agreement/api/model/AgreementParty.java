package se.sundsvall.agreement.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Agreement party model")
public class AgreementParty {

	@Schema(description = "Customer identifier at the supplier of the agreement", example = "81471222", accessMode = READ_ONLY)
	private String customerId;

	@ArraySchema(schema = @Schema(implementation = Agreement.class, accessMode = READ_ONLY))
	private List<Agreement> agreements;

	public static AgreementParty create() {
		return new AgreementParty();
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public AgreementParty withCustomerId(String customerId) {
		this.customerId = customerId;
		return this;
	}

	public List<Agreement> getAgreements() {
		return agreements;
	}

	public void setAgreements(List<Agreement> agreements) {
		this.agreements = agreements;
	}

	public AgreementParty withAgreements(List<Agreement> agreements) {
		this.agreements = agreements;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(agreements, customerId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgreementParty other = (AgreementParty) obj;
		return Objects.equals(agreements, other.agreements) && Objects.equals(customerId, other.customerId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AgreementParty [customerId=").append(customerId).append(", agreements=").append(agreements)
				.append("]");
		return builder.toString();
	}
}
