package com.tsm.sendemail.parser;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.resources.ClientResource;
import com.tsm.sendemail.util.ClientTestBuilder;

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

        // Do test
        Client result = parser.toModel(resource);

        // Assertions
        assertNotNull(result);
        assertThat(resource, allOf(
            hasProperty("id", nullValue()),
            hasProperty("name", is(resource.getName())),
            hasProperty("token", is(resource.getToken())),
            hasProperty("status", is(resource.getStatus())),
            hasProperty("hosts", notNullValue()),
            hasProperty("status", is(ClientStatus.ACTIVE.name()))));

        assertThat(result.getClientHosts().size(), is(resource.getHosts().size()));
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
        assertThat(result, allOf(
            hasProperty("id", is(client.getId())),
            hasProperty("name", is(result.getName())),
            hasProperty("token", is(result.getToken())),
            hasProperty("status", is(result.getStatus())),
            hasProperty("hosts", notNullValue()),
            hasProperty("status", is(ClientStatus.ACTIVE.name()))));

        assertThat(result.getHosts().size(), is(client.getClientHosts().size()));
    }
}
