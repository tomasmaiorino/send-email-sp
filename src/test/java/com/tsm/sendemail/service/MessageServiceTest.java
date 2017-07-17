package com.tsm.sendemail.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.sendemail.exceptions.ResourceNotFoundException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.model.Message.MessageStatus;
import com.tsm.sendemail.repository.MessageRepository;
import com.tsm.sendemail.util.ClientTestBuilder;
import com.tsm.sendemail.util.MessageTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class MessageServiceTest {

	@InjectMocks
	private MessageService service;

	@Mock
	private MessageRepository mockRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullMessageGiven_ShouldThrowException() {
		// Set up
		Message message = null;

		// Do test
		try {
			service.save(message);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void save_ValidMessageGiven_ShouldCreateMessage() {
		// Set up
		Message message = MessageTestBuilder.buildModel();

		// Expectations
		when(mockRepository.save(message)).thenReturn(message);

		// Do test
		Message result = service.save(message);

		// Assertions
		verify(mockRepository).save(message);
		assertNotNull(result);
		assertThat(result, is(message));
	}

	@Test
	public void findByIdAndClient_NullIdGiven_ShouldThrowException() {
		// Set up
		Long id = null;
		Client client = ClientTestBuilder.buildModel();

		// Do test
		try {
			service.findByIdAndClient(id, client);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	public void findByIdAndClient_NullClientGiven_ShouldThrowException() {
		// Set up
		Long id = 1l;
		Client client = null;

		// Do test
		try {
			service.findByIdAndClient(id, client);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	public void findByIdAndClient_NotFoundMessageGiven_ShouldThrowException() {
		// Set up
		Long id = 1l;
		Client client = ClientTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByIdAndClient(id, client)).thenReturn(Optional.empty());

		// Do test
		try {
			service.findByIdAndClient(id, client);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockRepository).findByIdAndClient(id, client);
	}

	@Test
	public void findByIdAndClient_FoundMessageGiven_ShouldReturnMessage() {
		// Set up
		Long id = 1l;
		Client client = ClientTestBuilder.buildModel();
		Message message = MessageTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByIdAndClient(id, client)).thenReturn(Optional.of(message));

		// Do test
		Message result = service.findByIdAndClient(id, client);

		// Assertions
		verify(mockRepository).findByIdAndClient(id, client);

		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", nullValue()), hasProperty("message", is(message.getMessage())),
						hasProperty("subject", is(message.getSubject())),
						hasProperty("senderName", is(message.getSenderName())),
						hasProperty("senderEmail", is(message.getSenderEmail())),
						hasProperty("status", is(MessageStatus.CREATED))));
	}

	//

	@Test
	public void findByClientStatusAndCreatedBetween_NullClientStatusGiven_ShouldThrowException() {
		// Set up
		ClientStatus status = null;
		LocalDateTime initialDate = LocalDateTime.now();
		LocalDateTime finalDate = LocalDateTime.now();

		// Do test
		try {
			service.findByClientStatusAndCreatedBetween(status, initialDate, finalDate);

			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	public void findByClientStatusAndCreatedBetween_NullInitialDateGiven_ShouldThrowException() {
		// Set up
		ClientStatus status = ClientStatus.ACTIVE;
		LocalDateTime initialDate = null;
		LocalDateTime finalDate = LocalDateTime.now();

		// Do test
		try {
			service.findByClientStatusAndCreatedBetween(status, initialDate, finalDate);

			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	public void findByClientStatusAndCreatedBetween_NullFinalDateGiven_ShouldThrowException() {
		// Set up
		ClientStatus status = ClientStatus.ACTIVE;
		LocalDateTime initialDate = LocalDateTime.now();
		LocalDateTime finalDate = null;

		// Do test
		try {
			service.findByClientStatusAndCreatedBetween(status, initialDate, finalDate);

			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyNoMoreInteractions(mockRepository);
	}

	@Test
	public void findByClientStatusAndCreatedBetween_NotFoundMessagesGiven_ShouldEmptyContent() {
		// Set up
		ClientStatus status = ClientStatus.ACTIVE;
		LocalDateTime initialDate = LocalDateTime.now();
		LocalDateTime finalDate = LocalDateTime.now();

		// Expectations
		when(mockRepository.findByClientStatusAndCreatedBetween(status, initialDate, finalDate))
				.thenReturn(Collections.emptySet());

		// Do test
		Set<Message> result = service.findByClientStatusAndCreatedBetween(status, initialDate, finalDate);

		// Assertions
		verify(mockRepository).findByClientStatusAndCreatedBetween(status, initialDate, finalDate);

		assertNotNull(result);
		assertThat(result.isEmpty(), is(true));
	}

	public void findByClientStatusAndCreatedBetween_FounddMessagesGiven_ShouldContent() {
		// Set up
		ClientStatus status = ClientStatus.ACTIVE;
		LocalDateTime initialDate = LocalDateTime.now();
		LocalDateTime finalDate = LocalDateTime.now();
		Message message = MessageTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByClientStatusAndCreatedBetween(status, initialDate, finalDate))
				.thenReturn(Collections.singleton(message));

		// Do test
		Set<Message> result = service.findByClientStatusAndCreatedBetween(status, initialDate, finalDate);

		// Assertions
		verify(mockRepository).findByClientStatusAndCreatedBetween(status, initialDate, finalDate);

		assertNotNull(result);
		assertThat(result.isEmpty(), is(false));
		assertThat(result.contains(message), is(true));
	}
}
