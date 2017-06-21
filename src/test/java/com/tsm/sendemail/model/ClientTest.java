package com.tsm.sendemail.model;

import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import com.tsm.sendemail.model.Client.ClientStatus;

@FixMethodOrder(MethodSorters.JVM)
public class ClientTest {

    private String name;
    private String token;
    private Set<ClientHosts> clientHosts;
    private ClientStatus status;

    @Before
    public void setUp() {
        name = random(100, true, true);
        token = random(100, true, true);
        clientHosts = new HashSet<>();
        clientHosts.add(bluildClientHost());
        status = ClientStatus.ACTIVE;
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

    private void buildClient() {
        buildClient(name, token, clientHosts, status);
    }

    private void buildClient(final String name, final String token, final Set<ClientHosts> clientHosts, final ClientStatus status) {
        Client client = new Client();
        client.setName(name);
        client.setToken(token);
        client.setClientHosts(clientHosts);
        client.setClientStatus(status);
    }

    private ClientHosts bluildClientHost() {
        // TODO Auto-generated method stub
        return null;
    }

}
