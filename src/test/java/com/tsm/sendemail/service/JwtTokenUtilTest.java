package com.tsm.sendemail.service;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public class JwtTokenUtilTest {

	private JwtTokenUtil jwtTokenUtil;

	@Before
	public void setUp() {
		jwtTokenUtil = new JwtTokenUtil();
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_doGenerateToken_NullSubjectGiven_ShouldThrowException() {
		// Set up
		String subject = null;

		// Do test
		jwtTokenUtil.doGenerateToken(subject);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_doGenerateToken_EmptySubjectGiven_ShouldThrowException() {
		// Set up
		String subject = "";

		// Do test
		jwtTokenUtil.doGenerateToken(subject);
	}

	@Test
	public void test_doGenerateToken_ValidSubjectGiven_ShouldReturnToken() {
		// Set up
		String subject = random(10, true, true);
		String result = null;

		// Do test
		result = jwtTokenUtil.doGenerateToken(subject);

		// Assertions
		assertNotNull(result);
		assertThat(result.isEmpty(), is(false));
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_getExpirationDateFromToken_NullTokenGiven_ShouldThrowException() {
		// Set up
		String token = null;

		// Do test
		jwtTokenUtil.getExpirationDateFromToken(token);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_getExpirationDateFromToken_EmptyTokenGiven_ShouldThrowException() {
		// Set up
		String token = "";

		// Do test
		jwtTokenUtil.getExpirationDateFromToken(token);
	}

	@Test
	public void test_getExpirationDateFromToken_ValidTokenGiven_ShouldReturnDate() {
		// Set up
		String subject = random(10, true, true);
		String token = jwtTokenUtil.doGenerateToken(subject);
		Date result = null;

		// Do test
		result = jwtTokenUtil.getExpirationDateFromToken(token);

		// Assertions
		assertNotNull(result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_getUserEmailFromToken_NullTokenGiven_ShouldThrowException() {

		String token = null;

		// Do test
		jwtTokenUtil.getUserEmailFromToken(token);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_getUserEmailFromToken_EmptyTokenGiven_ShouldThrowException() {

		String token = "";

		// Do test
		jwtTokenUtil.getUserEmailFromToken(token);
	}

	@Test
	public void test_getUserEmailFromToken_ValidTokenGiven_ShouldReturnUser() {
		// Set up
		String subject = random(10, true, true);
		String token = jwtTokenUtil.doGenerateToken(subject);
		String result = null;

		// Do test
		result = jwtTokenUtil.getUserEmailFromToken(token);

		// Assertions
		assertNotNull(result);
		assertThat(result.isEmpty(), is(false));
	}
}
