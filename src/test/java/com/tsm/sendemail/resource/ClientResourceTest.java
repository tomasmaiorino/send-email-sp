package com.tsm.sendemail.resource;

import static com.tsm.sendemail.util.ErrorCodes.FIELD_REQUIRED;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_EMAIL;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_NAME_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_TOKEN_SIZE;
import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.Collections;
import java.util.function.Supplier;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

import com.tsm.sendemail.resources.BaseResource;
import com.tsm.sendemail.util.ClientTestBuilder;

public class ClientResourceTest extends BaseResourceTest {

	private static final String LARGE_NAME = random(31, true, true);
	private static final String SMALL_NAME = random(1, true, true);

	private static final String LARGE_TOKEN = random(31, true, true);
	private static final String SMALL_TOKEN = random(1, true, true);
	private static final String RESOURCE_INVALID_EMAIL = random(31, true, true);

	private Supplier<? extends BaseResource> buildResourceFunction = ClientTestBuilder::buildResoure;

	@Before
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

	}

	@Test
	public void build_NullNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", null, FIELD_REQUIRED);
	}

	@Test
	public void build_SmallNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", SMALL_NAME, INVALID_NAME_SIZE);
	}

	@Test
	public void build_LargeNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", LARGE_NAME, INVALID_NAME_SIZE);
	}

	@Test
	public void build_EmptyNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", "", INVALID_NAME_SIZE);
	}

	//

	@Test
	public void build_NullTokenGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "token", null, FIELD_REQUIRED);
	}

	@Test
	public void build_SmallTokenGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "token", SMALL_TOKEN, INVALID_TOKEN_SIZE);
	}

	@Test
	public void build_LargeTokenGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "token", LARGE_TOKEN, INVALID_TOKEN_SIZE);
	}

	@Test
	public void build_EmptyTokenGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "token", "", INVALID_TOKEN_SIZE);
	}

	//

	@Test
	public void build_NullEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", null, FIELD_REQUIRED);
	}

	@Test
	public void build_InvalidEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", RESOURCE_INVALID_EMAIL, INVALID_EMAIL);
	}

	@Test
	public void build_EmptyEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", "", FIELD_REQUIRED);
	}

	//

	@Test
	public void build_NullHostsGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "hosts", null, FIELD_REQUIRED);
	}

	@Test
	public void build_EmptyHostsGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "hosts", Collections.emptySet(), FIELD_REQUIRED);
	}
	
	//TODO validate hosts content
}
