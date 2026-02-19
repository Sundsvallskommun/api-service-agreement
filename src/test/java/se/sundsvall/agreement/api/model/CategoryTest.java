package se.sundsvall.agreement.api.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.agreement.api.model.Category.COMMUNICATION;
import static se.sundsvall.agreement.api.model.Category.DISTRICT_COOLING;
import static se.sundsvall.agreement.api.model.Category.DISTRICT_HEATING;
import static se.sundsvall.agreement.api.model.Category.ELECTRICITY;
import static se.sundsvall.agreement.api.model.Category.ELECTRICITY_TRADE;
import static se.sundsvall.agreement.api.model.Category.WASTE_MANAGEMENT;
import static se.sundsvall.agreement.api.model.Category.WATER;

class CategoryTest {

	@Test
	void testCategoryEnum() {
		assertThat(Category.values()).containsExactly(
			COMMUNICATION,
			DISTRICT_COOLING,
			DISTRICT_HEATING,
			ELECTRICITY,
			ELECTRICITY_TRADE,
			WASTE_MANAGEMENT,
			WATER);
	}
}
