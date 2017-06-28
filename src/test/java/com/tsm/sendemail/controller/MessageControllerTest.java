package com.tsm.sendemail.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tsm.sendemail.exceptions.MessageException;
import com.tsm.sendemail.exceptions.ResourceNotFoundException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.ClientHosts;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.model.Message.MessageStatus;
import com.tsm.sendemail.parser.MessageParser;
import com.tsm.sendemail.resources.MessageResource;
import com.tsm.sendemail.service.ClientService;
import com.tsm.sendemail.service.MessageService;
import com.tsm.sendemail.service.SendEmailService;
import com.tsm.sendemail.util.ClientTestBuilder;
import com.tsm.sendemail.util.MessageTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class MessageControllerTest {

	private static final String CLIENT_TOKEN = ClientTestBuilder.CLIENT_TOKEN;

	private static final StringBuffer VALID_HOST = new StringBuffer(
			"http://localhost:8080/api/v1/messages/" + CLIENT_TOKEN);

	@Mock
	private MessageService mockService;

	@Mock
	private ClientService mockClientService;

	@Mock
	private MessageParser mockParser;

	@InjectMocks
	private MessagesController controller;

	@Mock
	private Validator validator;

	@Mock
	private MockHttpServletRequest request;

	@Mock
	private SendEmailService mockSendEmailService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(request.getRequestURL()).thenReturn(VALID_HOST);
		when(request.getRequestURI()).thenReturn("/api/v1/messages/" + CLIENT_TOKEN);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	public void save_InvalidMessageResourceGiven_ShouldThrowException() {
		// Set up
		MessageResource resource = MessageTestBuilder.buildResoure();

		// Expectations
		when(validator.validate(resource, Default.class)).thenThrow(new ValidationException());

		// Do test
		try {
			controller.save(CLIENT_TOKEN, resource, request);
			fail();
		} catch (ValidationException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verifyZeroInteractions(mockService, mockClientService, mockParser, mockSendEmailService);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void save_InvalidClientGiven_ShouldThrowException() {
		// Set up
		MessageResource resource = MessageTestBuilder.buildResoure();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockClientService.findByToken(CLIENT_TOKEN)).thenThrow(ResourceNotFoundException.class);

		// Do test
		try {
			controller.save(CLIENT_TOKEN, resource, request);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockClientService).findByToken(CLIENT_TOKEN);
		verifyZeroInteractions(mockService, mockParser, mockSendEmailService);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void save_ErrorSendingMessage_ShouldThrowException() {
		// Set up
		Message message = MessageTestBuilder.buildModel();
		Client client = ClientTestBuilder.buildModel();
		Set<ClientHosts> clientHosts = ClientTestBuilder.buildClientHost("http://localhost:8080");
		client.setClientHosts(clientHosts);
		MessageResource resource = MessageTestBuilder.buildResoure();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockClientService.findByToken(CLIENT_TOKEN)).thenReturn(client);
		when(mockParser.toModel(resource, client)).thenReturn(message);
		when(mockService.save(message)).thenReturn(message);
		when(mockSendEmailService.sendTextEmail(message)).thenThrow(MessageException.class);
		when(mockService.update(message)).thenReturn(message);

		// Do test
		try {
			controller.save(CLIENT_TOKEN, resource, request);
			fail();
		} catch (MessageException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockClientService).findByToken(CLIENT_TOKEN);
		verify(mockSendEmailService).sendTextEmail(message);
		verify(mockService).save(message);
		verify(mockParser).toModel(resource, client);
	}

	@Test
	public void save_ValidMessageResourceGiven_ShouldSaveMessage() {
		// Set up
		MessageResource resource = MessageTestBuilder.buildResoure();
		Message message = MessageTestBuilder.buildModel();
		Client client = ClientTestBuilder.buildModel();
		Set<ClientHosts> clientHosts = ClientTestBuilder.buildClientHost("http://localhost:8080");
		client.setClientHosts(clientHosts);

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockParser.toModel(resource, client)).thenReturn(message);
		when(mockClientService.findByToken(CLIENT_TOKEN)).thenReturn(client);
		when(mockSendEmailService.sendTextEmail(message)).thenReturn(message);
		when(mockService.save(message)).thenReturn(message);
		when(mockParser.toResource(message)).thenReturn(resource);

		// Do test
		MessageResource result = controller.save(CLIENT_TOKEN, resource, request);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).save(message);
		verify(mockParser).toModel(resource, client);
		verify(mockService).save(message);
		verify(mockParser).toResource(message);

		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", notNullValue()), hasProperty("message", is(resource.getMessage())),
						hasProperty("subject", is(resource.getSubject())),
						hasProperty("senderName", is(resource.getSenderName())),
						hasProperty("senderEmail", is(resource.getSenderEmail())),
						hasProperty("status", is(MessageStatus.CREATED.name()))));
	}
}
