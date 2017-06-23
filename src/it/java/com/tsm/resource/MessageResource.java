package com.tsm.resource;

import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.Objects;

import com.tsm.sendemail.resources.BaseResource;

import lombok.Getter;
import lombok.Setter;

public class MessageResource extends BaseResource {

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

	public MessageResource message(final String message) {
		this.message = message;
		return this;
	}

	public MessageResource message() {
		return message(random(100, true, true));
	}

	public MessageResource subject() {
		return message(random(20, true, true));
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
		return senderEmail(random(20, true, true));
	}

	public MessageResource senderEmail(final String senderEmail) {
		this.senderEmail = senderEmail;
		return this;
	}

}
