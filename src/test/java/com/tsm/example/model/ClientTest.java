package com.tsm.example.model;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.util.ReflectionTestUtils;

import com.tsm.example.model.Client;
import com.tsm.example.model.ClientAttribute;
import com.tsm.example.model.ClientHosts;
import com.tsm.example.model.Client.ClientStatus;
import com.tsm.example.util.ClientAttributeTestBuilder;
import com.tsm.example.util.ClientTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ClientTest {

	private String name;
	private String token;
	private Set<ClientHosts> clientHosts;
	private ClientStatus status;
	private String emailRecipient;
	private String email;

	@Before
	public void setUp() {
		name = random(100, true, true);
		token = random(100, true, true);
		clientHosts = new HashSet<>();
		clientHosts.add(bluildClientHost());
		status = ClientStatus.ACTIVE;
		emailRecipient = ClientTestBuilder.CLIENT_EMAIL_RECEIPIENT;
		email = ClientTestBuilder.CLIENT_EMAIL;
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullNameGiven_ShouldThrowException() {

		// Set up
		name = null;

		// Do test
		buildClient();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyNameGiven_ShouldThrowException() {

		// Set up
		name = "";

		// Do test
		buildClient();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullTokenGiven_ShouldThrowException() {

		// Set up
		token = null;

		// Do test
		buildClient();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyTokenGiven_ShouldThrowException() {

		// Set up
		token = "";

		// Do test
		buildClient();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullClientHostsGiven_ShouldThrowException() {

		// Set up
		clientHosts = null;

		// Do test
		buildClient();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullStatusGiven_ShouldThrowException() {

		// Set up
		status = null;

		// Do test
		buildClient();
	}

	//

	@Test(expected = IllegalArgumentException.class)
	public void build_NullEmailGiven_ShouldThrowException() {

		// Set up
		email = null;

		// Do test
		buildClient();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyEmailGiven_ShouldThrowException() {

		// Set up
		email = "";

		// Do test
		buildClient();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullEmailRecipientGiven_ShouldThrowException() {

		// Set up
		emailRecipient = null;

		// Do test
		buildClient();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyEmailRecipientGiven_ShouldThrowException() {

		// Set up
		emailRecipient = "";

		// Do test
		buildClient();
	}

	@Test(expected = IllegalArgumentException.class)
	public void setClientAttribute_NullClientAttributeGiven_ShouldThrowException() {
		// Set up
		Client client = buildClient();

		// Do test
		client.setClientAttribute(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getClientAttributeValueByKey_NullKeyGiven_ShouldThrowException() {
		// Set up
		Client client = buildClient();
		String key = null;

		// Do test
		client.getClientAttributeValueByKey(key);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getClientAttributeValueByKey_EmtpyKeyGiven_ShouldThrowException() {
		// Set up
		Client client = buildClient();
		String key = "";

		// Do test
		client.getClientAttributeValueByKey(key);
	}

	@Test
	public void getClientAttributeValueByKey_EmtpyClientAttributeGiven_ShouldThrowException() {
		// Set up
		Client client = buildClient();
		String key = "key";

		// Do test
		String result = client.getClientAttributeValueByKey(key);

		// Assertions
		assertNull(result);
	}

	@Test
	public void getClientAttributeValueByKey_EmtpyNullClientAttributeGiven_ShouldThrowException() {
		// Set up
		Client client = buildClient();
		ReflectionTestUtils.setField(client, "clientAttributes", null);
		String key = "key";

		// Do test
		String result = client.getClientAttributeValueByKey(key);

		// Assertions
		assertNull(result);
	}

	@Test
	public void getClientAttributeValueByKey_NotFoundClientAttributeKeyGiven_ShouldThrowException() {
		// Set up
		Client client = buildClient();
		client.addAttribute(ClientAttributeTestBuilder.buildModel());
		client.addAttribute(ClientAttributeTestBuilder.buildModel());

		// Do test
		String result = client.getClientAttributeValueByKey(ClientAttributeTestBuilder.getKey());

		// Assertions
		assertNull(result);
	}

	@Test
	public void getClientAttributeValueByKey_ValidClientAttributeKeyGiven_ShouldReturnContent() {
		// Set up
		String key = "key";
		String val = "val";
		Client client = buildClient();
		ClientAttribute ca = ClientAttributeTestBuilder.buildModel(client, key, val);
		ca.setClient(client);
		ca.setKey(key);
		ca.setValue(val);
		Set<ClientAttribute> cas = new HashSet<>();
		cas.add(ca);
		client.setClientAttribute(cas);

		// Do test
		String result = client.getClientAttributeValueByKey(key);

		// Assertions
		assertNotNull(result);
		assertThat(result, is(val));
	}

	@Test(expected = IllegalArgumentException.class)
	public void addContentAttribute_NullRuleGiven_ShouldThrowException() {

		// Set up
		Client client = buildClient();
		ClientAttribute clientAttribute = null;

		// Do test
		client.addAttribute(clientAttribute);
	}

	@Test
	public void addContentAttribute_ValidRuleGiven_ShouldAddRule() {

		// Set up
		Client client = buildClient();
		ClientAttribute clientAttribute = ClientAttributeTestBuilder.buildModel();

		// Do test
		client.addAttribute(clientAttribute);

		// Assertions
		assertThat(client.getClientAttributes().isEmpty(), is(false));
		assertThat(client.getClientAttributes().contains(clientAttribute), is(true));
	}

	private Client buildClient() {
		return ClientTestBuilder.buildModel(name, token, clientHosts, status, email, emailRecipient);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void build_AllValuesGiven_AllValuesShouldSet() {
		// Set up
		Client result = buildClient();

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", nullValue()), hasProperty("name", is(name)), hasProperty("token", is(token)),
						hasProperty("emailRecipient", is(emailRecipient)), hasProperty("email", is(email)),
						hasProperty("clientHosts", notNullValue()), hasProperty("status", is(status))));
	}

	private ClientHosts bluildClientHost() {
		// TODO Auto-generated method stub
		return null;
	}

}
