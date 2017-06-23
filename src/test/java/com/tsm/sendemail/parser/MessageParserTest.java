package com.tsm.sendemail.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.util.ReflectionTestUtils;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.model.Message.MessageStatus;
import com.tsm.sendemail.resources.MessageResource;
import com.tsm.sendemail.util.ClientTestBuilder;
import com.tsm.sendemail.util.MessageTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class MessageParserTest {

	private MessageParser parser = new MessageParser();

	@Test(expected = IllegalArgumentException.class)
	public void toModel_NullResourceGiven_ShouldThrowException() {
		// Set up
		MessageResource resource = null;
		Client client = ClientTestBuilder.buildModel();

		// Do test
		parser.toModel(resource, client);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toModel_NullClientGiven_ShouldThrowException() {
		// Set up
		MessageResource resource = MessageTestBuilder.buildResoure();
		Client client = null;

		// Do test
		parser.toModel(resource, client);
	}

	@Test
	public void toModel_ValidResourceGiven_ShouldCreateMessageModel() {
		// Set up
		MessageResource resource = MessageTestBuilder.buildResoure();
		Client client = ClientTestBuilder.buildModel();

		// Do test
		Message result = parser.toModel(resource, client);

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", nullValue()), hasProperty("message", is(resource.getMessage())),
						hasProperty("subject", is(resource.getSubject())),
						hasProperty("senderName", is(resource.getSenderName())),
						hasProperty("senderEmail", is(resource.getSenderEmail())),
						hasProperty("status", is(MessageStatus.CREATED))));

	}

	@Test(expected = IllegalArgumentException.class)
	public void toResource_NullMessageGiven_ShouldThrowException() {
		// Set up
		Message client = null;

		// Do test
		parser.toResource(client);
	}

	@Test
	public void toResource_ValidMessageGiven_ShouldCreateResourceModel() {
		// Set up
		Message messsage = MessageTestBuilder.buildModel();
		ReflectionTestUtils.setField(messsage, "id", MessageTestBuilder.MESSAGE_ID);

		// Do test
		MessageResource result = parser.toResource(messsage);

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", is(messsage.getId())), hasProperty("message", is(messsage.getMessage())),
						hasProperty("subject", is(messsage.getSubject())),
						hasProperty("senderName", is(messsage.getSenderName())),
						hasProperty("senderEmail", is(messsage.getSenderEmail())),
						hasProperty("status", is(MessageStatus.CREATED.name()))));

	}
}
