package com.tsm.it.controller;

import static com.jayway.restassured.RestAssured.given;
import static com.tsm.sendemail.util.ClientTestBuilder.LARGE_NAME;
import static com.tsm.sendemail.util.ClientTestBuilder.LARGE_TOKEN;
import static com.tsm.sendemail.util.ClientTestBuilder.RESOURCE_INVALID_EMAIL;
import static com.tsm.sendemail.util.ClientTestBuilder.SMALL_NAME;
import static com.tsm.sendemail.util.ClientTestBuilder.SMALL_TOKEN;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
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
import com.tsm.it.resource.ClientResource;
import com.tsm.sendemail.SendEmailApplication;
import com.tsm.sendemail.util.ClientTestBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SendEmailApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class ClientsControllerIT extends BaseTestIT {

	private static final String NAME_PARAM = "name";

	private static final String CLIENTS_REPORT_URL = "/api/v1/clients/report";

	private static final String EMAIL_RECIPIENT_PARAM = "emailRecipient";

	private static final String EMAIL_PARAM = "email";

	private static final String TOKEN_PARAM = "token";

	private static final String CLIENTS_URL_POST = "/api/v1/clients";

	@LocalServerPort
	private int port;

	@Value(value = "${it.test.email}")
	private String itTestEmail;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	protected static Map<String, String> header = null;

	@Test
	public void save_NoneHeaderGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields();

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST).then()
				.statusCode(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	public void save_InvalidTokenGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields();
		getTokenHeader();
		getHeader().put(AUTHORIZATION_KEY, ClientTestBuilder.CLIENT_TOKEN);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST).then()
				.statusCode(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void save_NullNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().name(null);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The name is required."), MESSAGE_FIELD_KEY, is(NAME_PARAM));
	}

	@Test
	public void save_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().name("");

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_NAME_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is(NAME_PARAM));
	}

	@Test
	public void save_SmallNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().name(SMALL_NAME);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_NAME_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is(NAME_PARAM));
	}

	@Test
	public void save_LargeNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_NAME_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is(NAME_PARAM));
	}

	//
	@Test
	public void save_NullTokenGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().token(null);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The token is required."), MESSAGE_FIELD_KEY, is(TOKEN_PARAM));
	}

	@Test
	public void save_EmptyTokenGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().token("");

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_TOKEN_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is(TOKEN_PARAM));
	}

	@Test
	public void save_SmallTokenGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().token(SMALL_TOKEN);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_TOKEN_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is(TOKEN_PARAM));
	}

	@Test
	public void save_LargeTokenGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().token(LARGE_TOKEN);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_TOKEN_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is(TOKEN_PARAM));
	}

	//
	@Test
	public void save_NullEmailGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().email(null);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(EMAIL_REQUIRED_MESSAGE), MESSAGE_FIELD_KEY, is(EMAIL_PARAM));
	}

	@Test
	public void save_EmptyEmailGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().email("");

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(EMAIL_REQUIRED_MESSAGE), MESSAGE_FIELD_KEY, is(EMAIL_PARAM));
	}

	@Test
	public void save_InvalidEmailGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().email(RESOURCE_INVALID_EMAIL);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("Invalid email."), MESSAGE_FIELD_KEY, is(EMAIL_PARAM));
	}

	//
	@Test
	public void save_NullEmailRecipientGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().emailRecipient(null);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is(EMAIL_RECIPIENT_REQUIRED_MESSAGE), MESSAGE_FIELD_KEY, is(EMAIL_RECIPIENT_PARAM));
	}

	@Test
	public void save_EmptyEmailRecipientGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().emailRecipient("");

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is(EMAIL_RECIPIENT_REQUIRED_MESSAGE), MESSAGE_FIELD_KEY, is(EMAIL_RECIPIENT_PARAM));
	}

	@Test
	public void save_InvalidEmailRecipientGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().emailRecipient(RESOURCE_INVALID_EMAIL);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("Invalid email."), MESSAGE_FIELD_KEY, is(EMAIL_RECIPIENT_PARAM));
	}

	//
	@Test
	public void save_InvalidStatusGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().status(INVALID_STATUS);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY, is(INVALID_STATUS));
	}

	@Test
	public void save_DuplicatedTokenGiven_ShouldReturnError() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields().headers(getTokenHeader()).create();
		String token = resource.getToken();
		ClientResource newResource = ClientResource.build().token(token).assertFields();

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_FIELD, is("Duplicated token."));
	}

	@Test
	public void save_ValidResourceGiven_ShouldSaveClient() {
		// Set Up
		ClientResource resource = ClientResource.build().assertFields();

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.CREATED.value()).body(NAME_PARAM, is(resource.getName()), TOKEN_PARAM,
						is(resource.getToken()), "status", is(resource.getStatus()), "attributes", nullValue(), "id",
						notNullValue(), "hosts.size()", is(resource.getHosts().size()), EMAIL_PARAM,
						is(resource.getEmail()));
	}

	@Test
	public void save_ValidResourceWithAttributesGiven_ShouldSaveClient() {
		// Set Up
		ClientResource resource = ClientResource.build().attributes().assertFields();

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.CREATED.value()).body(NAME_PARAM, is(resource.getName()), TOKEN_PARAM,
						is(resource.getToken()), "status", is(resource.getStatus()), "attributes.size()",
						is(resource.getAttributes().size()), "id", notNullValue(), "hosts.size()",
						is(resource.getHosts().size()), EMAIL_PARAM, is(resource.getEmail()));
	}

	@Test
	public void save_ValidResourceWithOneInvalidAttributeGiven_ShouldSaveClient() {
		// Set Up
		ClientResource resource = ClientResource.build().attributes(3).assertFields();
		resource.getAttributes().entrySet().iterator().next().setValue(null);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CLIENTS_URL_POST)
				.then().statusCode(HttpStatus.CREATED.value()).body(NAME_PARAM, is(resource.getName()), TOKEN_PARAM,
						is(resource.getToken()), "status", is(resource.getStatus()), "attributes.size()",
						is(resource.getAttributes().size() - 1), "id", notNullValue(), "hosts.size()",
						is(resource.getHosts().size()), EMAIL_PARAM, is(resource.getEmail()));
	}

	@Test
	public void generateReport_ValidClientGiven_ShouldSendReport() {
		// Set Up
		ClientResource.build().headers(getTokenHeader()).emailRecipient(itTestEmail).isAdmin(true).create();

		// Do Test
		given().headers(getTokenHeader()).when().get(CLIENTS_REPORT_URL).then().statusCode(HttpStatus.OK.value());
	}

	@Test
	@Ignore
	public void generateReport_NoneClientAdminGiven_ShouldReturnError() {

		// Do Test
		given().headers(getTokenHeader()).when().get(CLIENTS_REPORT_URL).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY, is("Report not sent."));

	}

}
