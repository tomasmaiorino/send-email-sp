package com.tsm.it.resource;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.Map;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class MessageResource extends BaseItResource {

	public static MessageResource build() {
		return new MessageResource();
	}

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private String message;

	@Getter
	@Setter
	private String subject;

	@Getter
	@Setter
	private String senderName;

	@Getter
	@Setter
	private String senderEmail;

	@Getter
	@Setter
	private String status;


	public MessageResource assertFields() {
		if (Objects.isNull(message)) {
			message();
		}
		if (Objects.isNull(senderEmail)) {
			senderEmail();
		}
		if (Objects.isNull(senderName)) {
			senderName();
		}
		if (Objects.isNull(subject)) {
			subject();
		}
		return this;
	}

	public MessageResource headers(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public MessageResource message(final String message) {
		this.message = message;
		return this;
	}

	public MessageResource message() {
		return message(random(100, true, true));
	}

	public MessageResource subject() {
		return subject(random(20, true, true));
	}

	public MessageResource subject(final String subject) {
		this.subject = subject;
		return this;
	}

	public MessageResource senderName() {
		return senderName(random(20, true, true));
	}

	public MessageResource senderName(final String senderName) {
		this.senderName = senderName;
		return this;
	}

	public MessageResource senderEmail() {
		return senderEmail(random(20, true, true) + "@" + random(2, true, false) + ".com");
	}

	public MessageResource senderEmail(final String senderEmail) {
		this.senderEmail = senderEmail;
		return this;
	}

	public MessageResource create(final String clientToken) {
		assertFields();
		return given().headers(getHeaders()).contentType("application/json").body(this).when()
				.post("/api/v1/messages/{clientToken}", clientToken).as(MessageResource.class);
	}

}
