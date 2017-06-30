package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
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
import com.tsm.resource.MessageResource;
import com.tsm.sendemail.SendEmailApplication;
import com.tsm.sendemail.model.Message.MessageStatus;
import com.tsm.sendemail.util.ClientTestBuilder;
import com.tsm.sendemail.util.MessageTestBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SendEmailApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class MessagesControllerIT {

	@LocalServerPort
	private int port;

	private String host;

	@Before
	public void setUp() {
		RestAssured.port = port;
		host = "http://localhost:" + port;
	}

	@Test
	public void save_NullMessageGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields().message(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("A value must be informed."));
	}

	@Test
	public void save_EmptyMessageGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields().message("");

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The message must be between 2 and 300 characters."));
	}

	@Test
	public void save_SmallMessageGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields().message("m");

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The message must be between 2 and 300 characters."));
	}

	@Test
	public void save_LargeMessageGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields().message(MessageTestBuilder.LARGE_MESSAGE);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The message must be between 2 and 300 characters."));
	}

	//
	@Test
	public void save_NullSubjectGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields().subject(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("A value must be informed."));
	}

	@Test
	public void save_EmptySubjectGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields().subject("");

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The subject must be between 2 and 30 characters."));
	}

	@Test
	public void save_SmallSubjectGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields().subject(MessageTestBuilder.SMALL_SUBJECT);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The subject must be between 2 and 30 characters."));
	}

	@Test
	public void save_LargeSubjectGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields().subject(MessageTestBuilder.LARGE_SUBJECT);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The subject must be between 2 and 30 characters."));
	}

	//

	@Test
	public void save_NullSenderNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields().senderName(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("A value must be informed."));
	}

	@Test
	public void save_EmptySenderNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields().senderName("");

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The sender name must be between 2 and 20 characters."));
	}

	@Test
	public void save_SmallSenderNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields()
				.senderName(MessageTestBuilder.SMALL_SENDER_NAME);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The sender name must be between 2 and 20 characters."));
	}

	@Test
	public void save_LargeSenderNameGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields()
				.senderName(MessageTestBuilder.LARGE_SENDER_NAME);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The sender name must be between 2 and 20 characters."));
	}

	//

	@Test
	public void save_NullSenderEmailGiven_ShouldReturnError() {
		// Set Up
		ClientResource client = ClientResource.build().create();
		MessageResource resource = MessageResource.build().assertFields().senderEmail(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("A value must be informed."));
	}

	@Test
	public void save_InvalidSenderEmailGiven_ShouldReturnError() {
		// Set Up
		Set<String> hosts = new HashSet();
		hosts.add(host);
		ClientResource client = ClientResource.build().hosts(hosts).create();
		MessageResource resource = MessageResource.build().assertFields().senderEmail("");

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("Invalid email."));
	}

	@Test
	public void save_NotFoundClientGiven_ShouldReturnError() {
		// Set Up
		MessageResource resource = MessageResource.build().assertFields();

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", ClientTestBuilder.CLIENT_TOKEN).then()
				.statusCode(HttpStatus.NOT_FOUND.value()).body("message", is("Client not found."));
	}

	@Test
	public void save_SendEmailClientGiven_ShouldSendEmail() {
		// Set Up
		Set<String> hosts = new HashSet();
		hosts.add(host);
		ClientResource client = ClientResource.build().emailRecipient("tomasmaiorino@gmail.com").hosts(hosts).create();
		MessageResource resource = MessageResource.build().assertFields();

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when()
				.post("/api/v1/messages/{clientToken}", client.getToken()).then().statusCode(HttpStatus.OK.value())
				.body("message", is(resource.getMessage())).body("status", is(MessageStatus.SENT.name()))
				.body("senderName", is(resource.getSenderName())).body("senderEmail", is(resource.getSenderEmail()))
				.body("subject", is(resource.getSubject()));
	}

}