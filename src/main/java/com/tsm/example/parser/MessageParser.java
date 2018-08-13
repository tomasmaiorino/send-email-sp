package com.tsm.example.parser;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.tsm.example.model.Client;
import com.tsm.example.model.Message;
import com.tsm.example.resources.MessageResource;

@Component
public class MessageParser implements IParser<MessageResource, Message> {

	public Message toModel(final MessageResource resource, final Client client) {
		Assert.notNull(resource, "The resource must not be null!");
		Assert.notNull(client, "The client must not be null!");
		Message message = new Message();
		message.setClient(client);
		message.setMessage(resource.getMessage());
		message.setSenderEmail(resource.getSenderEmail());
		;
		message.setSenderName(resource.getSenderName());
		message.setSubject(resource.getSubject());
		return message;
	}

	@Override
	public Message toModel(MessageResource resource) {
		return null;
	}

	public MessageResource toResource(final Message message) {
		Assert.notNull(message, "The message must not be null!");
		MessageResource resource = new MessageResource();
		resource.setId(message.getId());
		resource.setSubject(message.getSubject());
		resource.setMessage(message.getMessage());
		resource.setStatus(message.getStatus().name());
		resource.setSenderEmail(message.getSenderEmail());
		resource.setSenderName(message.getSenderName());
		resource.setSubject(message.getSubject());
		return resource;

	}

	@Override
	public Set<MessageResource> toResources(final Set<Message> models) {
		Assert.notEmpty(models, "The models must not be null nor empty!");
		return models.stream().map(m -> toResource(m)).collect(Collectors.toSet());
	}

}
