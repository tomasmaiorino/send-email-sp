package com.tsm.sendemail.resource;

import com.tsm.sendemail.resources.BaseResource;
import com.tsm.sendemail.resources.LoginResource;
import com.tsm.sendemail.util.LoginTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.function.Supplier;

import static com.tsm.sendemail.util.ErrorCodes.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.JVM)
public class LoginResourceTest extends BaseResourceTest {

	private Supplier<? extends BaseResource> buildResourceFunction = LoginTestBuilder::buildLoginResource;

	@Before
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

	}

	@Test
	@Ignore
	public void build_NullPasswordGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "password", null, REQUIRED_LOGIN_PASSWORD);
	}


	@Test
	public void build_EmptyPasswordGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "password", "", REQUIRED_LOGIN_PASSWORD);
	}

	//

	@Test
	public void build_NullEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", null, REQUIRED_LOGIN_EMAIL);
	}

	@Test
	public void build_InvalidEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", LoginTestBuilder.getInvalidEmail(), INVALID_LOGIN_EMAIL);
	}

	@Test
	public void build_EmptyEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", "", REQUIRED_LOGIN_EMAIL);
	}

	@Test
	public void build_AllValuesGiven_AllValuesShouldSet() {
		// Set up
		String password = LoginTestBuilder.getPassword();
		String email = LoginTestBuilder.getValidEmail();

		LoginResource result = LoginTestBuilder.buildResource(email, password);

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("email", is(email)),
						hasProperty("password", is(password))));
	}
}
