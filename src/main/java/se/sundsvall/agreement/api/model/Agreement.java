package se.sundsvall.agreement.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Objects;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Agreement model")
public class Agreement {

	@Schema(description = "Customer identifier at the supplier of the agreement", example = "81471222", accessMode = READ_ONLY)
	private String customerId;

	@Schema(description = "Agreement identifier", example = "223344-A", accessMode = READ_ONLY)
	private String agreementId;

	@Schema(description = "Billing identifier", example = "111222333", accessMode = READ_ONLY)
	private String billingId;

	@Schema(implementation = Category.class, accessMode = READ_ONLY)
	private Category category;

	@Schema(description = "Description", example = "The master agreement", accessMode = READ_ONLY)
	private String description;

	@Schema(description = "Id of the facility connected to the agreement", example = "1223334", accessMode = READ_ONLY)
	private String facilityId;

	@Schema(description = "Signal indicating whether the agreement is the main agreement or not", example = "true", accessMode = READ_ONLY)
	private boolean mainAgreement;

	@Schema(description = "Signal indicating whether the agreement has a binding period or not", example = "true", accessMode = READ_ONLY)
	private boolean binding;

	@Schema(description = "Description of the binding rule in cases where the agreement has a binding period", nullable = true, example = "12 mån bindning", accessMode = READ_ONLY)
	private String bindingRule;

	@Schema(description = "Start date of the agreement", example = "2022-01-01", accessMode = READ_ONLY)
	private LocalDate fromDate;

	@Schema(description = "End date of the agreement", example = "2022-12-31", accessMode = READ_ONLY)
	private LocalDate toDate;

	@Schema(description = "Signal if the agreement is active or not", example = "true", accessMode = READ_ONLY)
	private boolean active;

	public static Agreement create() {
		return new Agreement();
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Agreement withCustomerId(String customerId) {
		this.customerId = customerId;
		return this;
	}

	public String getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}

	public Agreement withAgreementId(String agreementId) {
		this.agreementId = agreementId;
		return this;
	}

	public String getBillingId() {
		return billingId;
	}

	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}

	public Agreement withBillingId(String billingId) {
		this.billingId = billingId;
		return this;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Agreement withCategory(Category category) {
		this.category = category;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Agreement withDescription(String description) {
		this.description = description;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public Agreement withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public boolean isMainAgreement() {
		return mainAgreement;
	}

	public void setMainAgreement(boolean mainAgreement) {
		this.mainAgreement = mainAgreement;
	}

	public Agreement withMainAgreement(boolean mainAgreement) {
		this.mainAgreement = mainAgreement;
		return this;
	}

	public boolean isBinding() {
		return binding;
	}

	public void setBinding(boolean binding) {
		this.binding = binding;
	}

	public Agreement withBinding(boolean binding) {
		this.binding = binding;
		return this;
	}

	public String getBindingRule() {
		return bindingRule;
	}

	public void setBindingRule(String bindingRule) {
		this.bindingRule = bindingRule;
	}

	public Agreement withBindingRule(String bindingRule) {
		this.bindingRule = bindingRule;
		return this;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public Agreement withFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
		return this;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public Agreement withToDate(LocalDate toDate) {
		this.toDate = toDate;
		return this;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Agreement withActive(boolean active) {
		this.active = active;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(active, agreementId, billingId, binding, bindingRule, category, customerId, description, facilityId,
			fromDate, mainAgreement, toDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Agreement other = (Agreement) obj;
		return active == other.active && Objects.equals(agreementId, other.agreementId)
			&& Objects.equals(billingId, other.billingId) && binding == other.binding
			&& Objects.equals(bindingRule, other.bindingRule) && category == other.category
			&& Objects.equals(description, other.description) && Objects.equals(facilityId, other.facilityId)
			&& Objects.equals(fromDate, other.fromDate) && mainAgreement == other.mainAgreement
			&& Objects.equals(toDate, other.toDate) && Objects.equals(customerId, other.customerId);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Agreement{");
		sb.append("customerId='").append(customerId).append('\'');
		sb.append(", agreementId='").append(agreementId).append('\'');
		sb.append(", billingId='").append(billingId).append('\'');
		sb.append(", category=").append(category);
		sb.append(", description='").append(description).append('\'');
		sb.append(", facilityId='").append(facilityId).append('\'');
		sb.append(", mainAgreement=").append(mainAgreement);
		sb.append(", binding=").append(binding);
		sb.append(", bindingRule='").append(bindingRule).append('\'');
		sb.append(", fromDate=").append(fromDate);
		sb.append(", toDate=").append(toDate);
		sb.append(", active=").append(active);
		sb.append('}');
		return sb.toString();
	}
}
