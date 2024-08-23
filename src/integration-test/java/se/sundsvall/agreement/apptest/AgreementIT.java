package se.sundsvall.agreement.apptest;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;

import se.sundsvall.agreement.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

/**
 * Read agreements tests
 */
@WireMockAppTestSuite(files = "classpath:/agreement/", classes = Application.class)
class AgreementIT extends AbstractAppTest {

	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getFacilitysActiveAgreements() {

		setupCall()
			.withServicePath("/2281/agreements/DISTRICT_COOLING/7112702050?onlyActive=true")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getFacilitysActiveMainAndSupplementaryAgreements() {

		setupCall()
			.withServicePath("/2281/agreements/ELECTRICITY/735999109116016144")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_getFacilitysAllAgreements() {

		setupCall()
			.withServicePath("/2281/agreements/DISTRICT_COOLING/7112702050?onlyActive=false")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_getAgreementsForFacilityWithNoActiveAgreements() {

		setupCall()
			.withServicePath("/2281/agreements/DISTRICT_COOLING/7112702055")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(NOT_FOUND)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test05_getAgreementsForFacilityWithNoAgreements() {

		setupCall()
			.withServicePath("/2281/agreements/ELECTRICITY_TRADE/731588001866455800?onlyActive=false")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(NOT_FOUND)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test06_getActiveAgreementsForParty() {

		setupCall()
			.withServicePath("/2281/agreements/485F409A-CB65-47EE-968A-33736365E139")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test07_getActiveAgreementsForSpecificCategoryForParty() {

		setupCall()
			.withServicePath("/2281/agreements/485F409A-CB65-47EE-968A-33736365E139?category=WASTE_MANAGEMENT")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test08_getActiveAgreementsForSpecificCategoryForPartyWithOnlyHistoricalAgreements() {

		setupCall()
			.withServicePath("/2281/agreements/524EEBC1-15B3-9CE6-BF11-D2B5F1D47C22?category=DISTRICT_COOLING")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(NOT_FOUND)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test09_getAllAgreementsForParty() {

		setupCall()
			.withServicePath("/2281/agreements/485F409A-CB65-47EE-968A-33736365E139?onlyActive=false")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test10_getAllAgreementsForSpecificCategoryForParty() {

		setupCall()
			.withServicePath("/2281/agreements/485F409A-CB65-47EE-968A-33736365E139?category=ELECTRICITY_TRADE&onlyActive=false")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test11_getAllAgreementsForPartyWithNoAgreements() {

		setupCall()
			.withServicePath("/2281/agreements/413AEBB2-BCCF-4447-B25E-D2B5F1D47C38?onlyActive=false")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(NOT_FOUND)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test12_getFacilitysAgreementsMultipleCalls() {

		setupCall()
			.withServicePath("/2281/agreements/DISTRICT_COOLING/7112702050?onlyActive=false")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test13_getAgreementsForPartyMultipleCalls() {

		setupCall()
			.withServicePath("/2281/agreements/485F409A-CB65-47EE-968A-33736365E139?onlyActive=false")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test14_getPagedAgreementsForParty() {

		setupCall()
			.withServicePath("/2281/paged/agreements/ff02dab0-8f08-43a4-9734-831fc7571246?onlyActive=false")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test15_getPagedAgreementsForPartyAndCategoryAll() {

		setupCall()
			.withServicePath("/2281/paged/agreements/b4ed77db-306c-49f0-a3d4-b1aa32092ba8?category=ELECTRICITY_TRADE&onlyActive=false")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test16_getPagedAgreementsForPartyAndCategoryOnlyActive() {

		setupCall()
			.withServicePath("/2281/paged/agreements/c1783f00-d992-4a03-93f4-16eb62be7364")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test17_getPagedAgreementsForPartyWithNoAgreements() {

		setupCall()
			.withServicePath("/2281/paged/agreements/bf0c239d-35e4-432d-b14d-843fce523583?onlyActive=false&limit=2&page=3")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
