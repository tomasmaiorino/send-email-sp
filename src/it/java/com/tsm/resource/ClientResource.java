package com.tsm.resource;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsm.sendemail.model.Client.ClientStatus;

import lombok.Getter;
import lombok.Setter;

public class ClientResource {

	public static ClientResource build() {
		return new ClientResource();
	}

	public ClientResource assertFields() {
		if (Objects.isNull(name)) {
			name();
		}
		if (Objects.isNull(hosts)) {
			hosts();
		}
		if (Objects.isNull(token)) {
			token();
		}
		if (Objects.isNull(email)) {
			email();
		}
		if (Objects.isNull(status)) {
			status();
		}
		if (Objects.isNull(emailRecipient)) {
			emailRecipient();
		}
		return this;
	}

	public ClientResource create() {
		assertFields();
		return given().headers(getHeaders()).contentType("application/json").body(this).when().post("/api/v1/clients")
				.as(ClientResource.class);
	}

	private ClientResource() {
	}

	@JsonIgnore
	@Getter
	@Setter
	private Map<String, String> headers;

	@Getter
	@Setter
	private Integer id;

	@Getter
	private String name;

	@Getter
	private String email;

	@Getter
	private String token;

	@Getter
	private String status;

	@Getter
	private Set<String> hosts;

	@Getter
	private String emailRecipient;

	public ClientResource emailRecipient(final String emailRecipient) {
		this.emailRecipient = emailRecipient;
		return this;
	}

	public ClientResource headers(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public ClientResource emailRecipient() {
		return emailRecipient(random(20, true, true) + "@" + random(2, true, false) + ".com");
	}

	public ClientResource email(final String email) {
		this.email = email;
		return this;
	}

	public ClientResource email() {
		return email(random(20, true, true) + "@" + random(2, true, false) + ".com");
	}

	public ClientResource token(final String token) {
		this.token = token;
		return this;
	}

	public ClientResource token() {
		return token(random(30, true, true));
	}

	public ClientResource status() {
		return status(ClientStatus.values()[RandomUtils.nextInt(0, ClientStatus.values().length - 1)].name());
	}

	public ClientResource status(final String status) {
		this.status = status;
		return this;
	}

	public ClientResource hosts() {
		Set<String> hosts = new HashSet<>();
		hosts.add("http://" + random(5, true, false) + ".com");
		hosts.add(random(5, true, false) + ".com");
		return hosts(hosts);
	}

	public ClientResource hosts(final Set<String> hosts) {
		this.hosts = hosts;
		return this;
	}

	public ClientResource name() {
		return name(random(30, true, true));
	}

	public ClientResource name(final String name) {
		this.name = name;
		return this;
	}
}
