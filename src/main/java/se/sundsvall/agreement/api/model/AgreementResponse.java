package se.sundsvall.agreement.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Agreement response model")
public class AgreementResponse {

	@ArraySchema(schema = @Schema(implementation = AgreementParty.class, accessMode = READ_ONLY))
	private List<AgreementParty> agreementParties;

	public static AgreementResponse create() {
		return new AgreementResponse();
	}

	public List<AgreementParty> getAgreementParties() {
		return agreementParties;
	}

	public void setAgreementParties(List<AgreementParty> agreementParties) {
		this.agreementParties = agreementParties;
	}

	public AgreementResponse withAgreementParties(List<AgreementParty> agreementParties) {
		this.agreementParties = agreementParties;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(agreementParties);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgreementResponse other = (AgreementResponse) obj;
		return Objects.equals(agreementParties, other.agreementParties);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AgreementResponse [agreementParties=").append(agreementParties).append("]");
		return builder.toString();
	}
}
