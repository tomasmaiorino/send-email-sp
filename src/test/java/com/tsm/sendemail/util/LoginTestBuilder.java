package com.tsm.sendemail.util;


import com.tsm.sendemail.resources.LoginResource;

public class LoginTestBuilder extends UserTestBuilder {

	public static LoginResource buildLoginResource() {
		return buildResource(getValidEmail(), getPassword());
	}

	public static LoginResource buildResource(final String validEmail, final String password) {
		LoginResource resource = new LoginResource();
		resource.setEmail(validEmail);
		resource.setPassword(password);
		return resource;
	}

}
