package se.sundsvall.agreement.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import se.sundsvall.dept44.models.api.paging.AbstractParameterPagingBase;

import java.util.Objects;

@Schema(description = "Agreement request parameters model")
public class AgreementParameters extends AbstractParameterPagingBase {

	@Schema(description = "Signal if only active or all agreements should be included in response, default is to only return active agreements.", example = "true")
	private boolean onlyActive = true;

	public static AgreementParameters create() {
		return new AgreementParameters();
	}

	public boolean isOnlyActive() {
		return onlyActive;
	}

	public void setOnlyActive(boolean onlyActive) {
		this.onlyActive = onlyActive;
	}

	public AgreementParameters withOnlyActive(boolean onlyActive) {
		this.onlyActive = onlyActive;
		return this;
	}

	public AgreementParameters withPage(int page){
		super.setPage(page);
		return this;
	}

	public AgreementParameters withLimit(int limit) {
		super.setLimit(limit);
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
		if (!super.equals(o)) {
			return false;
		}
		AgreementParameters that = (AgreementParameters) o;
		return onlyActive == that.onlyActive;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), onlyActive);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("AgreementParameters{");
		sb.append("onlyActive=").append(onlyActive);
		sb.append(", page=").append(page);
		sb.append(", limit=").append(limit);
		sb.append('}');
		return sb.toString();
	}
}
