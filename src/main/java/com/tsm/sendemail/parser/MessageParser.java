package com.tsm.sendemail.parser;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.resources.MessageResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Set;

@Component
public class MessageParser implements  IParser<MessageResource, Message> {

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
	public Set<MessageResource> toResources(Set<Message> models) {
		return null;
	}

}
