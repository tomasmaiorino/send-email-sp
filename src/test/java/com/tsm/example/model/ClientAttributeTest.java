package com.tsm.example.model;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.util.ReflectionTestUtils;

import com.tsm.example.model.Client;
import com.tsm.example.model.ClientAttribute;
import com.tsm.example.util.ClientAttributeTestBuilder;
import com.tsm.example.util.ClientTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ClientAttributeTest {

	private String key;
	private String value;
	private Client client;

	@Before
	public void setUp() {
		key = random(5, false, true);
		value = random(5, false, true);
		client = ClientTestBuilder.buildModel();
	}

	@Test
	public void build_RequiredValuesGiven_ValuesShouldSet() {
		// Do test
		ClientAttribute clientAttribute = build();

		// Assertions
		assertThat(clientAttribute,
				allOf(hasProperty("key", is(key)), hasProperty("value", is(value)), hasProperty("client", is(client))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullKeyGiven_ShouldThrowException() {
		// Set up
		key = null;

		// Do test
		build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyKeyGiven_ShouldThrowException() {
		// Set up
		key = "";

		// Do test
		build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyValueGiven_ShouldThrowException() {
		// Set up
		value = "";

		// Do test
		build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullValueGiven_ShouldThrowException() {
		// Set up
		value = null;

		// Do test
		build();
	}

	@Test
	public void equals_NullKeyGiven_ShouldReturnFalse() {
		// Set up
		ClientAttribute st = build();
		ReflectionTestUtils.setField(st, "key", null);

		ClientAttribute st2 = build();

		// Do test
		boolean result = st.equals(st2);
		boolean result2 = st2.equals(st);

		// Assertions
		assertThat(result, is(false));
		assertThat(result2, is(false));
	}

	@Test
	public void equals_ClientGiven_ShouldReturnFalse() {
		// Set up
		ClientAttribute st = build();
		ReflectionTestUtils.setField(st, "client", null);

		ClientAttribute st2 = build();

		// Do test
		boolean result = st.equals(st2);
		boolean result2 = st2.equals(st);

		// Assertions
		assertThat(result, is(false));
		assertThat(result2, is(false));
	}

	@Test
	public void equals_ValidClientAttributesGiven_ShouldReturnTrue() {
		// Set up
		ClientAttribute st = build();

		ClientAttribute st2 = build();

		// Do test
		boolean result = st.equals(st2);
		boolean result2 = st2.equals(st);

		// Assertions
		assertThat(result, is(true));
		assertThat(result2, is(true));
	}

	@Test
	public void equals_ValidDifferentKeyGiven_ShouldReturnTrue() {
		// Set up
		ClientAttribute st = build();
		st.setKey(key.toUpperCase());

		ClientAttribute st2 = build();

		// Do test
		boolean result = st.equals(st2);
		boolean result2 = st2.equals(st);

		// Assertions
		assertThat(result, is(true));
		assertThat(result2, is(true));
	}

	private ClientAttribute build() {
		return ClientAttributeTestBuilder.buildModel(client, key, value);
	}

}
