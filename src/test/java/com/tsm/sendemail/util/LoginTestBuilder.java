package com.tsm.sendemail.util;


import com.tsm.sendemail.resources.UserResource;

public class LoginTestBuilder extends UserTestBuilder {

	public static UserResource buildResource() {
		return buildResource(getValidEmail(), getPassword());
	}

	public static UserResource buildResource(final String validEmail, final String password) {
		UserResource resource = new UserResource();
		resource.setEmail(validEmail);
		resource.setPassword(password);
		return resource;
	}

}
