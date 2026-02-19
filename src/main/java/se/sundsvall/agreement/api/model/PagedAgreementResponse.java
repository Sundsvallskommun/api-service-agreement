package se.sundsvall.agreement.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import se.sundsvall.dept44.models.api.paging.PagingMetaData;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Schema(description = "Paged agreement response model")
public class PagedAgreementResponse {

	@ArraySchema(schema = @Schema(implementation = Agreement.class, accessMode = READ_ONLY))
	private List<Agreement> agreements;

	@JsonProperty("_meta")
	@Schema(implementation = PagingMetaData.class, accessMode = READ_ONLY)
	private PagingMetaData metaData;

	public static PagedAgreementResponse create() {
		return new PagedAgreementResponse();
	}

	public List<Agreement> getAgreements() {
		return agreements;
	}

	public void setAgreements(List<Agreement> agreements) {
		this.agreements = agreements;
	}

	public PagedAgreementResponse withAgreements(List<Agreement> agreements) {
		this.agreements = agreements;
		return this;
	}

	public PagingMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(PagingMetaData metaData) {
		this.metaData = metaData;
	}

	public PagedAgreementResponse withMetaData(PagingMetaData metaData) {
		this.metaData = metaData;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PagedAgreementResponse that = (PagedAgreementResponse) o;
		return Objects.equals(agreements, that.agreements) && Objects.equals(metaData, that.metaData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(agreements, metaData);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PagedAgreementResponse{");
		sb.append("agreements=").append(agreements);
		sb.append(", metaData=").append(metaData);
		sb.append('}');
		return sb.toString();
	}
}
