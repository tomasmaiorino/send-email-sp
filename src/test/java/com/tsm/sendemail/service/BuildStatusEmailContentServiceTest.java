package com.tsm.sendemail.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.model.Message.MessageStatus;
import com.tsm.sendemail.util.ClientTestBuilder;
import com.tsm.sendemail.util.MessageTestBuilder;

@FixMethodOrder(MethodSorters.JVM)

public class BuildStatusEmailContentServiceTest {

	@Mock
	private MessageService messageService;

	@InjectMocks
	private BuildStatusEmailContentService service;

	private LocalDateTime initialDate = null;
	private LocalDateTime finalDate = null;

	private Set<Client> activeClients = null;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		initialDate = LocalDateTime.now().minusHours(24);
		finalDate = LocalDateTime.now();
		activeClients = new HashSet<>();
		activeClients.add(ClientTestBuilder.buildModel());
	}

	@Test
	public void buildMessage_NullInitialDateGiven_ShouldThrowException() {
		// Set up
		initialDate = null;

		// Do test
		try {
			service.buildMessage(initialDate, finalDate, activeClients);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(messageService);
	}

	@Test
	public void buildMessage_NullFinalDateGiven_ShouldThrowException() {
		// Set up
		finalDate = null;

		// Do test
		try {
			service.buildMessage(initialDate, finalDate, activeClients);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(messageService);
	}

	@Test
	public void buildMessage_EmptyClientsGiven_ShouldThrowException() {
		// Set up
		activeClients = Collections.emptySet();

		// Do test
		try {
			service.buildMessage(initialDate, finalDate, activeClients);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(messageService);
	}

	@Test
	public void buildMessage_NoneMessagesFoundGiven_ShouldReturnNoneMessagesMessage() {

		Client client = activeClients.iterator().next();

		// Expectations
		when(messageService.findByClientAndCreatedBetween(client, initialDate, finalDate))
				.thenReturn(Collections.emptySet());

		// Do test
		String result = service.buildMessage(initialDate, finalDate, activeClients);

		// Assertions
		verify(messageService).findByClientAndCreatedBetween(activeClients.iterator().next(), initialDate, finalDate);
		assertNotNull(result);
		System.out.println(result);
		assertThat(result.contains(String.format("None messages has been sent to the client [%s].", client.getName())),
				is(true));
	}

	@Test
	public void buildMessage_MessagesFoundGiven_ShouldReturnMessages() {

		Client client = activeClients.iterator().next();
		Message message1 = MessageTestBuilder.buildModel();

		Message message2 = MessageTestBuilder.buildModel();
		message2.setStatus(MessageStatus.SENT);

		Message message3 = MessageTestBuilder.buildModel();
		message3.setStatus(MessageStatus.NOT_SENT);

		Message message4 = MessageTestBuilder.buildModel();
		message4.setStatus(MessageStatus.ERROR);

		Set<Message> messages = new HashSet<>();
		messages.add(message1);
		messages.add(message2);
		messages.add(message3);
		messages.add(message4);

		// Expectations
		when(messageService.findByClientAndCreatedBetween(client, initialDate, finalDate)).thenReturn(messages);

		// Do test
		String result = service.buildMessage(initialDate, finalDate, activeClients);

		// Assertions
		verify(messageService).findByClientAndCreatedBetween(activeClients.iterator().next(), initialDate, finalDate);
		assertNotNull(result);
		// System.out.println(result);
		assertThat(result.contains(String.format("Client: %s", client.getName())), is(true));
		assertThat(result.contains(String.format("Sent: %s", 1)), is(true));
		assertThat(result.contains(String.format("Created: %s", 1)), is(true));
		assertThat(result.contains(String.format("Not Sent: %s", 1)), is(true));
		assertThat(result.contains(String.format("Error: %s", 1)), is(true));
	}

	@Test
	public void buildMessage_MoreThanOneClientGiven_ShouldReturnMessages() {

		Client client2 = ClientTestBuilder.buildModel();
		Message message1 = MessageTestBuilder.buildModel();

		Message message2 = MessageTestBuilder.buildModel();
		message2.setStatus(MessageStatus.SENT);

		Message message3 = MessageTestBuilder.buildModel();
		message3.setStatus(MessageStatus.SENT);

		Set<Message> messagesClient2 = new HashSet<>();
		messagesClient2.add(message1);
		messagesClient2.add(message2);
		messagesClient2.add(message3);

		Client client = activeClients.iterator().next();
		message1 = MessageTestBuilder.buildModel();

		message2 = MessageTestBuilder.buildModel();
		message2.setStatus(MessageStatus.SENT);

		message3 = MessageTestBuilder.buildModel();
		message3.setStatus(MessageStatus.NOT_SENT);

		Message message4 = MessageTestBuilder.buildModel();
		message4.setStatus(MessageStatus.ERROR);

		Set<Message> messages = new HashSet<>();
		messages.add(message1);
		messages.add(message2);
		messages.add(message3);
		messages.add(message4);

		activeClients.add(client2);

		// Expectations
		when(messageService.findByClientAndCreatedBetween(client, initialDate, finalDate)).thenReturn(messages);
		when(messageService.findByClientAndCreatedBetween(client2, initialDate, finalDate)).thenReturn(messagesClient2);

		// Do test
		String result = service.buildMessage(initialDate, finalDate, activeClients);

		// Assertions
		verify(messageService).findByClientAndCreatedBetween(activeClients.iterator().next(), initialDate, finalDate);
		assertNotNull(result);
		// System.out.println(result);
		assertThat(result.contains(String.format("Client: %s", client.getName())), is(true));
		assertThat(result.contains(String.format("Sent: %s", 1)), is(true));
		assertThat(result.contains(String.format("Created: %s", 1)), is(true));
		assertThat(result.contains(String.format("Not Sent: %s", 0)), is(true));
	}

}
