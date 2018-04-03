package com.tsm.it.controller;

import static com.jayway.restassured.RestAssured.given;
import static com.tsm.sendemail.util.ClientTestBuilder.RESOURCE_INVALID_EMAIL;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.tsm.it.resource.UserResource;
import com.tsm.sendemail.SendEmailApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SendEmailApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class UsersControllerIT extends BaseTestIT {

	private static final String EMAIL_PARAM = "email";

	private static final String TOKEN_PARAM = "token";

	private static final String USER_AUTH_URL = "/api/v1/users/auth";

	@LocalServerPort
	private int port;

	@Value(value = "${it.test.email}")
	private String itTestEmail;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	protected static Map<String, String> header = null;

	//
	@Test
	public void save_NullEmailGiven_ShouldReturnError() {
		// Set Up
		UserResource resource = UserResource.build().assertFields().email(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post(USER_AUTH_URL).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(EMAIL_REQUIRED_MESSAGE), MESSAGE_FIELD_KEY, is(EMAIL_PARAM));
	}

	@Test
	public void save_EmptyEmailGiven_ShouldReturnError() {
		// Set Up
		UserResource resource = UserResource.build().assertFields().email("");

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post(USER_AUTH_URL).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(EMAIL_REQUIRED_MESSAGE), MESSAGE_FIELD_KEY, is(EMAIL_PARAM));
	}

	@Test
	public void save_InvalidEmailGiven_ShouldReturnError() {
		// Set Up
		UserResource resource = UserResource.build().assertFields().email(RESOURCE_INVALID_EMAIL);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post(USER_AUTH_URL).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("Invalid email."), MESSAGE_FIELD_KEY, is(EMAIL_PARAM));
	}

	//
	@Test
	public void save_NullPasswordGiven_ShouldReturnError() {
		// Set Up
		UserResource resource = UserResource.build().email();

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post(USER_AUTH_URL).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY, is("The password is required."));
	}

	@Test
	public void save_ValidResourceGiven_ShouldSaveClient() {
		// Set Up
		UserResource resource = UserResource.build().email(getUserItEmail()).password(getUserItPass());

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post(USER_AUTH_URL).then().body(TOKEN_PARAM,
				notNullValue());
	}

}
