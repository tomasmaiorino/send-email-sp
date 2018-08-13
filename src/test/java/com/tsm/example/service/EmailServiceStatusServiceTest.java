package com.tsm.example.service;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.example.model.Message;
import com.tsm.example.model.Client.ClientStatus;
import com.tsm.example.service.BuildStatusEmailContentService;
import com.tsm.example.service.ClientService;
import com.tsm.example.service.EmailServiceStatusService;
import com.tsm.example.service.MessageService;
import com.tsm.example.service.SendEmailService;

@FixMethodOrder(MethodSorters.JVM)
public class EmailServiceStatusServiceTest {

	@Mock
	private MessageService messageService;

	@Mock
	private SendEmailService sendEmailService;

	@Mock
	private ClientService clientService;

	@Mock
	private BuildStatusEmailContentService buildStatusEmailContentService;

	@InjectMocks
	private EmailServiceStatusService service;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void checkingDailyEmailsStatus_NullNoneClientsFoundGiven_ShouldReturnNull() {

		// Expectations
		when(clientService.findByStatus(ClientStatus.ACTIVE)).thenReturn(Collections.emptySet());

		Message result = service.checkingDailyEmailsStatus();

		// Assertions
		verify(clientService).findByStatus(ClientStatus.ACTIVE);
		verifyZeroInteractions(messageService, buildStatusEmailContentService, sendEmailService);

		assertNull(result);
	}
}
