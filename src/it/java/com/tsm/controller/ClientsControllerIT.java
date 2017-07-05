package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static com.tsm.sendemail.util.ClientTestBuilder.INVALID_STATUS;
import static com.tsm.sendemail.util.ClientTestBuilder.LARGE_NAME;
import static com.tsm.sendemail.util.ClientTestBuilder.LARGE_TOKEN;
import static com.tsm.sendemail.util.ClientTestBuilder.RESOURCE_INVALID_EMAIL;
import static com.tsm.sendemail.util.ClientTestBuilder.SMALL_NAME;
import static com.tsm.sendemail.util.ClientTestBuilder.SMALL_TOKEN;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.tsm.resource.ClientResource;
import com.tsm.sendemail.SendEmailApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SendEmailApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class ClientsControllerIT {

	@LocalServerPort
	private int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void save_NullNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().name(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("The name is required."));
	}

	@Test
	public void save_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().name("");

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The name must be between 2 and 30 characters."));
	}

	@Test
	public void save_SmallNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().name(SMALL_NAME);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The name must be between 2 and 30 characters."));
	}

	@Test
	public void save_LargeNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().name(LARGE_NAME);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The name must be between 2 and 30 characters."));
	}

	//
	@Test
	public void save_NullTokenGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().token(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("The token is required."));
	}

	@Test
	public void save_EmptyTokenGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().token("");

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The token must be between 2 and 50 characters."));
	}

	@Test
	public void save_SmallTokenGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().token(SMALL_TOKEN);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The token must be between 2 and 50 characters."));
	}

	@Test
	public void save_LargeTokenGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().token(LARGE_TOKEN);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The token must be between 2 and 50 characters."));
	}

	//
	@Test
	public void save_NullEmailGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().email(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("The email is required."));
	}

	@Test
	public void save_EmptyEmailGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().email("");

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("The email is required."));
	}

	@Test
	public void save_InvalidEmailGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().email(RESOURCE_INVALID_EMAIL);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("Invalid email."));
	}

	//
	@Test
	public void save_NullEmailRecipientGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().emailRecipient(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("The email recipient is required."));
	}

	@Test
	public void save_EmptyEmailRecipientGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().emailRecipient("");

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("The email recipient is required."));
	}

	@Test
	public void save_InvalidEmailRecipientGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().emailRecipient(RESOURCE_INVALID_EMAIL);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("Invalid email."));
	}

	//
	@Test
	public void save_InvalidStatusGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().status(INVALID_STATUS);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The status must be either 'INACTIVE' or 'ACTIVE'."));
	}

	@Test
	@Ignore
	public void save_DuplicatedTokenGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().create();
		String token = resource.getToken();
		ClientResource newResource = ClientResource.build().token(token).assertFields();

		// Do Test
		given().body(newResource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("Duplicated token."));
	}

	@Test
	public void save_ValidResourceGiven_ShouldSaveClient() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields();

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/clients").then()
				.statusCode(HttpStatus.CREATED.value()).body("name", is(resource.getName()))
				.body("token", is(resource.getToken())).body("status", is(resource.getStatus()))
				.body("id", notNullValue()).body("hosts.size()", is(resource.getHosts().size()))
				.body("email", is(resource.getEmail()));
	}

}
