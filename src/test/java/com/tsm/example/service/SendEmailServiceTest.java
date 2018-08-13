package com.tsm.example.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.example.exceptions.MessageException;
import com.tsm.example.model.Message;
import com.tsm.example.model.Message.MessageStatus;
import com.tsm.example.service.BaseSendEmailService;
import com.tsm.example.service.MessageService;
import com.tsm.example.service.SendEmailService;
import com.tsm.example.util.MessageTestBuilder;

@SuppressWarnings("unchecked")
@FixMethodOrder(MethodSorters.JVM)
public class SendEmailServiceTest {

	@Mock
	private MessageService messageService;

	@Mock
	private BaseSendEmailService sendEmailService;

	@InjectMocks
	private SendEmailService service;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void sendTextEmail_NullMessageGiven_ShouldThrowException() {
		// Set up
		Message message = null;

		// Do test
		try {
			service.sendTextEmail(message);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(messageService, sendEmailService);
	}

	@Test
	public void sendTextEmail_ErrorSendingEmail_ShouldThrowException() {
		// Set up
		Message message = MessageTestBuilder.buildModel();

		// Expectations
		when(sendEmailService.sendTextEmail(message)).thenThrow(MessageException.class);

		// Do test
		try {
			service.sendTextEmail(message);
			fail();
		} catch (MessageException e) {
		}

		// Assertions
		verify(sendEmailService).sendTextEmail(message);
		verify(messageService).update(message, message);

		assertThat(message.getStatus(), is(MessageStatus.ERROR));

	}

}
