package com.tsm.example.model;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.example.model.Client;
import com.tsm.example.model.ClientHosts;

@FixMethodOrder(MethodSorters.JVM)
public class ClientHostsTest {

    @Test(expected = IllegalArgumentException.class)
    public void build_NullHostGiven_ShouldThrowException() {
        // Set up
        String host = null;

        // Do test
        ClientHosts hosts = new ClientHosts();
        hosts.setHost(host);
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullClientGiven_ShouldThrowException() {
        // Set up
        Client client = null;

        // Do test
        ClientHosts hosts = new ClientHosts();
        hosts.setClient(client);
    }
}
