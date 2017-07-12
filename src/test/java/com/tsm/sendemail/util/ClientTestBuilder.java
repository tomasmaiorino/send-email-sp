package com.tsm.sendemail.util;

import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.model.ClientHosts;
import com.tsm.sendemail.resources.ClientResource;

public class ClientTestBuilder {

    public static final String LARGE_NAME = random(31, true, true);
    public static final String SMALL_NAME = random(1, true, true);

    public static final String LARGE_TOKEN = random(51, true, true);
    public static final String SMALL_TOKEN = random(1, true, true);
    public static final String RESOURCE_INVALID_EMAIL = random(31, true, true);
    public static final String INVALID_STATUS = "INVD";

    public static final String[] HOSTS = new String[] { "http://mysite.com.br" };
    public static final String CLIENT_NAME = random(30, true, true);
    public static final String CLIENT_TOKEN = random(30, true, true);
    public static final String CLIENT_EMAIL = "email@site.com";
    public static final String CLIENT_EMAIL_RECEIPIENT = "email@site.com";
    public static final Set<ClientHosts> CLIENT_HOSTS = buildClientHost();

    private static final ClientStatus STATUS = ClientStatus.ACTIVE;

    public static Set<ClientHosts> buildClientHost() {
        return buildClientHost(HOSTS);
    }

    public static Client buildModel() {
        return buildModel(CLIENT_NAME, CLIENT_TOKEN, CLIENT_HOSTS, STATUS, CLIENT_EMAIL, CLIENT_EMAIL_RECEIPIENT);
    }

    public static Client buildModel(final String name, final String token, final Set<ClientHosts> clientHosts,
        final ClientStatus status, final String email, final String emailRecipient) {
        Client client = new Client();
        client.setName(name);
        client.setToken(token);
        client.setClientHosts(clientHosts);
        client.setClientStatus(status);
        client.setEmail(email);
        client.setEmailRecipient(emailRecipient);
        return client;
    }

    public static Set<String> buildClientHost(Set<ClientHosts> hosts) {
        return hosts.stream().map(h -> h.getHost()).collect(Collectors.toSet());
    }

    public static Set<ClientHosts> buildClientHost(final String... hosts) {
        Set<ClientHosts> clientHosts = new HashSet<>(hosts.length);
        ClientHosts ch = null;
        for (String s : hosts) {
            ch = new ClientHosts();
            ch.setHost(s);
            clientHosts.add(ch);
        }
        return clientHosts;
    }

    public static ClientResource buildResource(final String name, final String token, final String email,
        final Set<String> hosts, final String status, final String emailRecipient) {
        ClientResource resource = new ClientResource();
        resource.setName(name);
        resource.setEmail(email);
        resource.setHosts(hosts);
        resource.setToken(token);
        resource.setStatus(status);
        resource.setEmailRecipient(emailRecipient);
        return resource;
    }

    public static ClientResource buildResoure() {
        return buildResource(CLIENT_NAME, CLIENT_TOKEN, CLIENT_EMAIL, buildClientHost(CLIENT_HOSTS),
            STATUS.toString(), CLIENT_EMAIL_RECEIPIENT);
    }
}
