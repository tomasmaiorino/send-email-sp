package com.tsm.it.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.tsm.it.resource.UserResource;

import lombok.Getter;
import lombok.Setter;

public abstract class BaseTestIT {

	public static Map<String, String> header = null;
	protected static final String REQUIRED_NAME = "The name is required.";
	protected static final String INVALID_STATUS = "The status must be either 'INACTIVE' or 'ACTIVE'.";
	protected static final String REQUIRED_STATUS = "The status is required.";
	protected static final String MESSAGE_FIELD = "message";
	protected static final String STATUS_KEY = "status";
	protected static final String MESSAGE_CHECK_KEY = "[0].message";
	protected static final String MESSAGE_FIELD_KEY = "[0].field";

	protected static final String AUTHORIZATION_KEY = "Authorization";

	protected static final String AUTHORIZATION_VALUE_PREFIX = "Bearer ";

	public static final String AUTH_URL = "/api/v1/users/auth";
	
	protected static final String EMAIL_REQUIRED_MESSAGE = "The email is required.";

	protected static final String INVALID_TOKEN_SIZE_MESSAGE = "The token must be between 2 and 50 characters.";

	protected static final String EMAIL_RECIPIENT_REQUIRED_MESSAGE = "The email recipient is required.";

	protected static final String INVALID_NAME_SIZE_MESSAGE = "The name must be between 2 and 30 characters.";

	@Value("${user.it.email}")
	@Getter
	@Setter
	private String userItEmail;

	@Value("${user.it.pass}")
	@Getter
	@Setter
	private String userItPass;

	public Map<String, String> getHeader() {
		if (header == null) {
			header = new HashMap<>();
		}
		return header;
	}

	protected Map<String, String> getTokenHeader() {
		UserResource userResource = UserResource.build().auth(getUserItEmail(), getUserItPass());
		getHeader().put(AUTHORIZATION_KEY, userResource.getToken());
		return getHeader();
	}

}
