package com.tsm.sendemail.model;

import com.tsm.sendemail.util.UserTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.JVM)
public class UserTest {

	public String name;

	public String email;

	public String password;

	public User.UserStatus status;

	@Before
	public void setUp() {
		name = UserTestBuilder.getName();
		status = UserTestBuilder.getCustomerStatus();
		email = UserTestBuilder.getValidEmail();
		password = UserTestBuilder.getPassword();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullNameGiven_ShouldThrowException() {

		// Set up
		name = null;

		// Do test
		buildCustomer();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyNameGiven_ShouldThrowException() {

		// Set up
		name = "";

		// Do test
		buildCustomer();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullPasswordGiven_ShouldThrowException() {

		// Set up
		password = null;

		// Do test
		buildCustomer();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyPasswordGiven_ShouldThrowException() {

		// Set up
		password = "";

		// Do test
		buildCustomer();
	}

	private User buildCustomer() {
		return UserTestBuilder.buildModel(name, email, password, status);

	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullStatusGiven_ShouldThrowException() {

		// Set up
		status = null;

		// Do test
		buildCustomer();
	}

	//

	@Test(expected = IllegalArgumentException.class)
	public void build_NullEmailGiven_ShouldThrowException() {

		// Set up
		email = null;

		// Do test
		buildCustomer();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyEmailGiven_ShouldThrowException() {

		// Set up
		email = "";

		// Do test
		buildCustomer();
	}

	@Test
	public void build_AllValuesGiven_AllValuesShouldSet() {
		// Set up
		User result = buildCustomer();

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", nullValue()), hasProperty("name", is(name)),
						hasProperty("password", is(password)), hasProperty("email", is(email)),
						hasProperty("status", is(status))));
	}

}
