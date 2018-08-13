package com.tsm.example.parser;

import com.tsm.example.model.Client;
import com.tsm.example.model.ClientAttribute;
import com.tsm.example.model.ClientHosts;
import com.tsm.example.model.Client.ClientStatus;
import com.tsm.example.resources.ClientResource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ClientParser implements  IParser<ClientResource, Client>{

	public Client toModel(final ClientResource resource) {
		Assert.notNull(resource, "The resource must not be null!");
		Client client = Client.ClientBuilder
				.Client(resource.getName(), resource.getEmail(), resource.getToken(),
						ClientStatus.valueOf(resource.getStatus()), resource.getEmailRecipient(), resource.getIsAdmin())
				.build();
		client.setClientHosts(loadClientHosts(client, resource.getHosts()));

		if (!CollectionUtils.isEmpty(resource.getAttributes())) {
			resource.getAttributes().forEach((k, v) -> {
				if (StringUtils.isNotBlank(v)) {
					client.addAttribute(buildClientAttribute(client, k, v));
				}
			});
		}
		return client;
	}

	private ClientAttribute buildClientAttribute(final Client client, final String k, final String v) {
		return ClientAttribute.ClientAttributeBuilder.ClientAttribute(client, k, v).build();
	}

	private Set<ClientHosts> loadClientHosts(final Client client, final Set<String> hosts) {
		Set<ClientHosts> clientHosts = new HashSet<>();
		hosts.forEach(h -> {
			ClientHosts host = new ClientHosts();
			host.setHost(h);
			host.setClient(client);
			clientHosts.add(host);
		});
		return clientHosts;
	}

	public ClientResource toResource(final Client client) {
		Assert.notNull(client, "The client must not be null!");
		ClientResource resource = new ClientResource();
		resource.setId(client.getId());
		resource.setEmail(client.getEmail());
		resource.setName(client.getName());
		resource.setToken(client.getToken());
		resource.setStatus(client.getStatus().toString());
		resource.setId(client.getId());
		resource.setHosts(loadHosts(client));
		resource.setEmailRecipient(client.getEmailRecipient());
		resource.setIsAdmin(client.getIsAdmin());
		if (!CollectionUtils.isEmpty(client.getClientAttributes())) {
			resource.setAttributes(client.getClientAttributes().stream()
					.collect(Collectors.toMap(ClientAttribute::getKey, ClientAttribute::getValue)));
		}
		return resource;

	}

	@Override
	public Set<ClientResource> toResources(Set<Client> models) {
		return null;
	}

	private Set<String> loadHosts(final Client client) {
		return client.getClientHosts().stream().map(ClientHosts::getHost).collect(Collectors.toSet());
	}

}
