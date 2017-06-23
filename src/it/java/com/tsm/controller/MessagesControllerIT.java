package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

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
import com.tsm.resource.MessageResource;
import com.tsm.sendemail.SendEmailApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SendEmailApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class MessagesControllerIT {

	@LocalServerPort
	private int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
    public void save_NullMessageGiven_ShouldReturnError() {
        // Set Up
        MessageResource resource = MessageResource.build().assertFields().message(null);
        //ClientResource client = ClientResource.build().create();

        // Do Test
        given().body(resource).contentType(ContentType.JSON).when().post("/api/v1/messages",01).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("A value must be informed."));
    }
}
