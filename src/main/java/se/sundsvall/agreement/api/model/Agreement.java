package se.sundsvall.agreement.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Objects;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Agreement model")
public class Agreement {

	@Schema(description = "Customer identifier at the supplier of the agreement", examples = "81471222", accessMode = READ_ONLY)
	private String customerId;

	@Schema(description = "Agreement identifier", examples = "223344-A", accessMode = READ_ONLY)
	private String agreementId;

	@Schema(description = "Billing identifier", examples = "111222333", accessMode = READ_ONLY)
	private String billingId;

	@Schema(implementation = Category.class, accessMode = READ_ONLY)
	private Category category;

	@Schema(description = "Description", examples = "The master agreement", accessMode = READ_ONLY)
	private String description;

	@Schema(description = "Id of the facility connected to the agreement", examples = "1223334", accessMode = READ_ONLY)
	private String facilityId;

	@Schema(description = "Signal indicating whether the agreement is the main agreement or not", examples = "true", accessMode = READ_ONLY)
	private boolean mainAgreement;

	@Schema(description = "Signal indicating whether the agreement has a binding period or not", examples = "true", accessMode = READ_ONLY)
	private boolean binding;

	@Schema(description = "Description of the binding rule in cases where the agreement has a binding period", nullable = true, examples = "12 mån bindning", accessMode = READ_ONLY)
	private String bindingRule;

	@Schema(description = "Placement status for agreement", examples = "Tillkopplad", accessMode = READ_ONLY)
	private String placementStatus;

	@Schema(description = "Net area id for agreement", examples = "SUV", accessMode = READ_ONLY)
	private String netAreaId;

	@Schema(description = "Site address connected to the agreement", examples = "Första gatan 2", accessMode = READ_ONLY)
	private String siteAddress;

	@Schema(description = "Signal if the agreement is a production agreement or not (can be null if not applicable)", examples = "true", nullable = true, accessMode = READ_ONLY)
	private Boolean production;

	@Schema(description = "Start date of the agreement", examples = "2022-01-01", accessMode = READ_ONLY)
	private LocalDate fromDate;

	@Schema(description = "End date of the agreement", examples = "2022-12-31", accessMode = READ_ONLY)
	private LocalDate toDate;

	@Schema(description = "Signal if the agreement is active or not", examples = "true", accessMode = READ_ONLY)
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

	public String getPlacementStatus() {
		return this.placementStatus;
	}

	public void setPlacementStatus(String placementStatus) {
		this.placementStatus = placementStatus;
	}

	public Agreement withPlacementStatus(String placementStatus) {
		this.placementStatus = placementStatus;
		return this;
	}

	public String getNetAreaId() {
		return netAreaId;
	}

	public void setNetAreaId(String netAreaId) {
		this.netAreaId = netAreaId;
	}

	public Agreement withNetAreaId(String netAreaId) {
		this.netAreaId = netAreaId;
		return this;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public Agreement withSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
		return this;
	}

	public Boolean getProduction() {
		return this.production;
	}

	public void setProduction(Boolean production) {
		this.production = production;
	}

	public Agreement withProduction(Boolean production) {
		this.production = production;
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
		return Objects.hash(active, agreementId, billingId, binding, bindingRule, category, customerId, description, facilityId, fromDate, mainAgreement, netAreaId, placementStatus, production, siteAddress, toDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof final Agreement other)) { return false; }
		return active == other.active && Objects.equals(agreementId, other.agreementId) && Objects.equals(billingId, other.billingId) && binding == other.binding && Objects.equals(bindingRule, other.bindingRule) && category == other.category && Objects
			.equals(customerId, other.customerId) && Objects.equals(description, other.description) && Objects.equals(facilityId, other.facilityId) && Objects.equals(fromDate, other.fromDate) && mainAgreement == other.mainAgreement && Objects.equals(
				netAreaId, other.netAreaId) && Objects.equals(placementStatus, other.placementStatus) && Objects.equals(production, other.production) && Objects.equals(siteAddress, other.siteAddress) && Objects.equals(toDate, other.toDate);
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		builder.append("Agreement [customerId=").append(customerId).append(", agreementId=").append(agreementId).append(", billingId=").append(billingId).append(", category=").append(category).append(", description=").append(description).append(
			", facilityId=").append(facilityId).append(", mainAgreement=").append(mainAgreement).append(", binding=").append(binding).append(", bindingRule=").append(bindingRule).append(", placementStatus=").append(placementStatus).append(", netAreaId=")
			.append(netAreaId).append(", siteAddress=").append(siteAddress).append(", production=").append(production).append(", fromDate=").append(fromDate).append(", toDate=").append(toDate).append(", active=").append(active).append("]");
		return builder.toString();
	}

}
