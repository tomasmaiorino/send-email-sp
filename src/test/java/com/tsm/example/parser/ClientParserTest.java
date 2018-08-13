package com.tsm.example.parser;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.example.model.Client;
import com.tsm.example.model.ClientAttribute;
import com.tsm.example.model.Client.ClientStatus;
import com.tsm.example.parser.ClientParser;
import com.tsm.example.resources.ClientResource;
import com.tsm.example.util.ClientAttributeTestBuilder;
import com.tsm.example.util.ClientTestBuilder;

@SuppressWarnings("unchecked")
@FixMethodOrder(MethodSorters.JVM)
public class ClientParserTest {

	private ClientParser parser = new ClientParser();

	@Test(expected = IllegalArgumentException.class)
	public void toModel_NullResourceGiven_ShouldThrowException() {
		// Set up
		ClientResource resource = null;

		// Do test
		parser.toModel(resource);
	}

	@Test
	public void toModel_ValidResourceGiven_ShouldCreateClientModel() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();
		resource.setIsAdmin(true);

		// Do test
		Client result = parser.toModel(resource);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", nullValue()), hasProperty("name", is(resource.getName())),
				hasProperty("token", is(resource.getToken())),
				hasProperty("emailRecipient", is(resource.getEmailRecipient())),
				hasProperty("clientAttributes", nullValue()), hasProperty("clientHosts", notNullValue()),
				hasProperty("isAdmin", is(resource.getIsAdmin())), hasProperty("status", is(ClientStatus.ACTIVE))));

		assertThat(result.getClientHosts().size(), is(resource.getHosts().size()));
	}

	@Test
	public void toModel_ValidClientAttributeResourceGiven_ShouldCreateClientModel() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();
		resource.setIsAdmin(true);
		Map<String, String> attributes = ClientAttributeTestBuilder.getAttributes(3);
		attributes.entrySet().iterator().next().setValue(null);
		resource.setAttributes(attributes);

		// Do test
		Client result = parser.toModel(resource);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", nullValue()), hasProperty("name", is(resource.getName())),
				hasProperty("token", is(resource.getToken())),
				hasProperty("emailRecipient", is(resource.getEmailRecipient())),
				hasProperty("clientHosts", notNullValue()), hasProperty("clientAttributes", notNullValue()),
				hasProperty("isAdmin", is(resource.getIsAdmin())), hasProperty("status", is(ClientStatus.ACTIVE))));

		assertThat(result.getClientHosts().size(), is(resource.getHosts().size()));
		// as one of the values in the map was set as null, the attribute must not be
		// created.
		assertThat(result.getClientAttributes().size(), is(2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void toResource_NullClientGiven_ShouldThrowException() {
		// Set up
		Client client = null;

		// Do test
		parser.toResource(client);
	}

	@Test
	public void toResource_ValidClientGiven_ShouldCreateResourceModel() {
		// Set up
		Client client = ClientTestBuilder.buildModel();

		// Do test
		ClientResource result = parser.toResource(client);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", is(client.getId())), hasProperty("name", is(client.getName())),
				hasProperty("token", is(client.getToken())),
				hasProperty("emailRecipient", is(client.getEmailRecipient())), hasProperty("attributes", nullValue()),
				hasProperty("hosts", notNullValue()), hasProperty("status", is(ClientStatus.ACTIVE.name()))));

		assertThat(result.getHosts().size(), is(client.getClientHosts().size()));
	}

	@Test
	public void toResource_ValidClientWithAttributesGiven_ShouldCreateResourceModel() {
		// Set up
		Client client = ClientTestBuilder.buildModel();
		ClientAttribute attribute = ClientAttributeTestBuilder.buildModel(client, ClientAttributeTestBuilder.getKey(),
				ClientAttributeTestBuilder.getValue());
		ClientAttribute attribute2 = ClientAttributeTestBuilder.buildModel(client, ClientAttributeTestBuilder.getKey(),
				ClientAttributeTestBuilder.getValue());
		client.addAttribute(attribute);
		client.addAttribute(attribute2);

		// Do test
		ClientResource result = parser.toResource(client);

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", is(client.getId())), hasProperty("name", is(client.getName())),
						hasProperty("token", is(client.getToken())),
						hasProperty("emailRecipient", is(client.getEmailRecipient())),
						hasProperty("attributes", notNullValue()), hasProperty("hosts", notNullValue()),
						hasProperty("status", is(ClientStatus.ACTIVE.name()))));

		assertThat(result.getHosts().size(), is(client.getClientHosts().size()));
		assertThat(result.getAttributes().size(), is(client.getClientAttributes().size()));
	}

}
