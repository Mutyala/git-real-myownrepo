package com.twinerestapi.com.twine;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.util.concurrent.TimeUnit;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

/**
 * Unit test for Twine Email Client application
 */
public class DemoApp {

	public String getUrl = "https://s3.us-east-2.amazonaws.com/twine-public/apis/twine-mail-get.json";
	public String putUrl = "https://s3.us-east-2.amazonaws.com/twine-public/apis/twine-mail-put.json";
	public String getUrl1 = "https://s3.us-east-2.amazonaws.com/twine-public/apis/twine-mail1-get.json";
	public String putUrl1 = "https://s3.us-east-2.amazonaws.com/twine-public/apis/twine-mail1-put.json";

	@Test
	public void checkResponseCodeForCorrectGetRequest() {
		given().when().get(getUrl).then().assertThat().statusCode(200);
	}

	@Test
	public void checkResponseCodeForIncorrectGetRequest() {
		given().when().get(getUrl1).then().assertThat().statusCode(403);
	}

	@Test
	public void checkGetResponseContentTypeJson() {
		given().when().get(getUrl).then().assertThat().contentType(ContentType.JSON);
	}

	@Test
	public void checkGetAPIResponseTime() {
		given().when().get(getUrl).then().assertThat().time(lessThan(3000l), TimeUnit.MILLISECONDS);
	}

	@Test
	public void checkGetReponseBody() {
		Response res1 = given().when().get(getUrl).then().extract().response();
		System.out.println(res1.asString());
	}

	@Test
	public void checkGetReponseElements() {
		given().when().get(getUrl).then().body("emails.unread", hasItems(true, false))
				.body("emails.any{it.containsKey('date')}", is(true));
	}

	@Test
	public void checkGetResponseOfTheFirstEmail() {
		given().when().get(getUrl).then().assertThat().body("emails.id[0]", equalTo(0))
				.body("emails.subject[0].size()", lessThan(50)).body("emails.body[0].size()", lessThan(100))
				.body("emails.from[0]", endsWith("gmail.com")).body("emails.unread[0]", equalTo(true));
	}

	@Test
	public void checkResponseCodeForCorrectPutRequest() {
		given().when().get(putUrl).then().assertThat().statusCode(200);
	}

	@Test
	public void checkResponseCodeForIncorrectPutRequest() {
		given().when().get(putUrl1).then().assertThat().statusCode(403);
	}

	@Test
	public void checkPutResponseContentTypeJson() {
		given().when().get(putUrl).then().assertThat().contentType(ContentType.JSON);
	}

	@Test
	public void checkPutAPIResponseTime() {
		given().when().get(putUrl).then().assertThat().time(lessThan(1000l), TimeUnit.MILLISECONDS);
	}

	@Test
	public void checkPutReponseBody() {
		Response res2 = given().when().get(putUrl).then().extract().response();
		System.out.println(res2.asString());
	}

	@Test
	public void checkPutReponseElements() {
		given().when().get(putUrl).then().body("unread", equalTo(false)).body("containsKey('date')", is(true));
	}

	@Test
	public void checkResponseOfTheEmailAfterTheUpdate() {
		given().when().get(putUrl).then().assertThat().body("id", equalTo(0)).body("subject.size()", lessThan(50))
				.body("body.size()", lessThan(100)).body("from", endsWith("gmail.com")).body("unread", equalTo(false));
	}

}
