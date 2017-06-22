package com.tsm.sendemail.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.repository.ClientRepository;
import com.tsm.sendemail.util.ClientTestBuilder;

public class ClientServiceTest {

	@InjectMocks
	private ClientService service;

	@Mock
	private ClientRepository mockRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullClientGiven_ShouldThrowException() {
		// Set up
		Client client = null;

		// Do test
		try {
			service.save(client);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void save_ValidClientGiven_ShouldCreateClient() {
		// Set up
		Client client = ClientTestBuilder.buildModel();

		// Expectations
		when(mockRepository.save(client)).thenReturn(client);

		// Do test
		Client result = service.save(client);

		// Assertions
		verify(mockRepository).save(client);
		assertNotNull(result);
		assertThat(result, is(client));
	}
}
