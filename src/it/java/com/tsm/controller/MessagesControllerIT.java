package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;
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

    private static String host = "http://localhost";

    @Value(value = "${it.test.email}")
    private String itTestEmail;

    @Value(value = "${client.service.key}")
    private String clientServiceKey;

    public static Map<String, String> clientHeader = null;

    public static Map<String, String> header = null;

    public static Map<String, String> getHeader() {
        if (header == null) {
            header = new HashMap<>();
            header.put("Referer", host);
        }
        return header;
    }

    public Map<String, String> getClientHeader() {
        if (clientHeader == null) {
            clientHeader = new HashMap<>();
            clientHeader.put(ClientsControllerIT.ADMIN_TOKEN_HEADER, clientServiceKey);
        }
        return clientHeader;
    }

    @Before
    public void setUp() {
        RestAssured.port = port;
        header = getHeader();
    }

    @Test
    public void checking_preflight_ShouldReturnValidHeader() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();

        // Do Test
        given().headers(header).when()
            .options("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.OK.value())
            .header("Access-Control-Allow-Origin", is(client.getHosts().iterator().next()))
            .header("Access-Control-Allow-Methods", is("POST, GET, OPTIONS"))
            .header("Access-Control-Max-Age", is("3600"))
            .header("Access-Control-Allow-Headers", is("Origin, X-Requested-With, Content-Type, Accept"))
            .header("Access-Control-Expose-Headers", is("Location"));
    }

    @Test
    public void save_NullMessageGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().message(null);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .header("Access-Control-Allow-Origin", is(client.getHosts().iterator().next()))
            .header("Access-Control-Allow-Methods", is("POST, GET, OPTIONS"))
            .header("Access-Control-Max-Age", is("3600"))
            .header("Access-Control-Allow-Headers", is("Origin, X-Requested-With, Content-Type, Accept"))
            .header("Access-Control-Expose-Headers", is("Location"))
            .body("[0].message", is("The message is required."), "[0].field", is("message"));
    }

    @Test
    public void save_EmptyMessageGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().message("");

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message",
                is("The message must be between 2 and 300 characters."), "[0].field", is("message"));
    }

    @Test
    public void save_SmallMessageGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().message("m");

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message",
                is("The message must be between 2 and 300 characters."), "[0].field", is("message"));
    }

    @Test
    public void save_LargeMessageGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().message(MessageTestBuilder.LARGE_MESSAGE);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message",
                is("The message must be between 2 and 300 characters."), "[0].field", is("message"));
    }

    //
    @Test
    public void save_NullSubjectGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().subject(null);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("[0].message", is("The subject is required."), "[0].field", is("subject"));
    }

    @Test
    public void save_EmptySubjectGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().subject("");

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message",
                is("The subject must be between 2 and 30 characters."), "[0].field", is("subject"));
    }

    @Test
    public void save_SmallSubjectGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().subject(MessageTestBuilder.SMALL_SUBJECT);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message",
                is("The subject must be between 2 and 30 characters."), "[0].field", is("subject"));
    }

    @Test
    public void save_LargeSubjectGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().subject(MessageTestBuilder.LARGE_SUBJECT);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message",
                is("The subject must be between 2 and 30 characters."), "[0].field", is("subject"));
    }

    //

    @Test
    public void save_NullSenderNameGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().senderName(null);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("[0].message", is("The sender name is required."), "[0].field", is("senderName"));
    }

    @Test
    public void save_EmptySenderNameGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().senderName("");

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message",
                is("The sender name must be between 2 and 20 characters."), "[0].field", is("senderName"));
    }

    @Test
    public void save_SmallSenderNameGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields()
            .senderName(MessageTestBuilder.SMALL_SENDER_NAME);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message",
                is("The sender name must be between 2 and 20 characters."), "[0].field", is("senderName"));
    }

    @Test
    public void save_LargeSenderNameGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields()
            .senderName(MessageTestBuilder.LARGE_SENDER_NAME);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message",
                is("The sender name must be between 2 and 20 characters."), "[0].field", is("senderName"));
    }

    //

    @Test
    public void save_NullSenderEmailGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getClientHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().senderEmail(null);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("[0].message", is("The sender email is required."), "[0].field", is("senderEmail"));
    }

    @Test
    public void save_InvalidSenderEmailGiven_ShouldReturnError() {
        // Set Up
        Set<String> hosts = new HashSet<>();
        hosts.add(host);
        ClientResource client = ClientResource.build().headers(getClientHeader()).hosts(hosts).create();
        MessageResource resource = MessageResource.build().assertFields().senderEmail("");

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("[0].message", is("Invalid email."), "[0].field", is("senderEmail"));
    }

    @Test
    public void save_NotFoundClientGiven_ShouldReturnError() {
        // Set Up
        MessageResource resource = MessageResource.build().assertFields();

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", ClientTestBuilder.CLIENT_TOKEN).then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void save_SendEmailClientGiven_ShouldSendEmail() {
        // Set Up
        Set<String> hosts = new HashSet<>();
        hosts.add(host);
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(getClientHeader())
            .hosts(hosts).create();
        MessageResource resource = MessageResource.build().assertFields();

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .post("/api/v1/messages/{clientToken}", client.getToken()).then().statusCode(HttpStatus.OK.value())
            .header("Access-Control-Allow-Origin", is(client.getHosts().iterator().next()))
            .header("Access-Control-Allow-Methods", is("POST, GET, OPTIONS"))
            .header("Access-Control-Max-Age", is("3600"))
            .header("Access-Control-Allow-Headers", is("Origin, X-Requested-With, Content-Type, Accept"))
            .header("Access-Control-Expose-Headers", is("Location"))
            .body("message", is(resource.getMessage())).body("status", is(MessageStatus.SENT.name()))
            .body("senderName", is(resource.getSenderName())).body("senderEmail", is(resource.getSenderEmail()))
            .body("subject", is(resource.getSubject()));
    }

    @Test
    public void findById_NotFoundClientGiven_ShouldReturnError() {
        // Set Up
        Set<String> hosts = new HashSet<>();
        hosts.add(host);
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(getClientHeader())
            .hosts(hosts).create();
        MessageResource resource = MessageResource.build().create(client.getToken());

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .get("/api/v1/messages/{clientToken}/{id}", ClientTestBuilder.CLIENT_TOKEN, resource.getId()).then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Ignore
    public void findById_NotFoundMessageGiven_ShouldReturnError() {
        // Set Up
        Set<String> hosts = new HashSet<>();
        hosts.add(host);
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(getClientHeader())
            .hosts(hosts).create();
        MessageResource resource = MessageResource.build().create(client.getToken());

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .get("/api/v1/messages/{clientToken}/{id}", client.getToken(), RandomUtils.nextLong(1000l, 10000l))
            .then().statusCode(HttpStatus.NOT_FOUND.value()).body("message", is("Message not found."));
    }

    @Test
    @Ignore
    public void findById_IdMessageFoundGiven_ShouldReturnMessage() {
        // Set Up
        Set<String> hosts = new HashSet<>();
        hosts.add(host);
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(getClientHeader())
            .hosts(hosts).create();
        MessageResource resource = MessageResource.build().create(client.getToken());

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
            .get("/api/v1/messages/{clientToken}/{id}", client.getToken(), resource.getId()).then()
            .statusCode(HttpStatus.OK.value()).body("message", is(resource.getMessage()))
            .body("status", is(MessageStatus.SENT.name())).body("senderName", is(resource.getSenderName()))
            .body("senderEmail", is(resource.getSenderEmail())).body("subject", is(resource.getSubject()));
    }

}
