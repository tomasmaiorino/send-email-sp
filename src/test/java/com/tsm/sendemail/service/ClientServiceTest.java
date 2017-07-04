package com.tsm.sendemail.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.exceptions.ResourceNotFoundException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.repository.ClientRepository;
import com.tsm.sendemail.util.ClientTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
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
	public void save_DuplicatedTokenGiven_ShouldThrowException() {
		// Set up
		Client client = ClientTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByToken(client.getToken())).thenReturn(Optional.of(client));

		// Do test
		try {
			service.save(client);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(mockRepository).findByToken(client.getToken());
		verify(mockRepository, times(0)).save(client);
	}

	@Test
	public void save_ValidClientGiven_ShouldCreateClient() {
		// Set up
		Client client = ClientTestBuilder.buildModel();

		// Expectations
		when(mockRepository.save(client)).thenReturn(client);
		when(mockRepository.findByToken(client.getToken())).thenReturn(Optional.empty());

		// Do test
		Client result = service.save(client);

		// Assertions
		verify(mockRepository).save(client);
		verify(mockRepository).findByToken(client.getToken());
		assertNotNull(result);
		assertThat(result, is(client));
	}

	@Test
	public void findByToken_NullClientTokenGiven_ShouldThrowException() {
		// Set up
		String token = null;

		// Do test
		try {
			service.findByToken(token);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findByToken_EmptyClientTokenGiven_ShouldThrowException() {
		// Set up
		String token = "";

		// Do test
		try {
			service.findByToken(token);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findByToken_ClientNotFound_ShouldThrowException() {
		// Set up
		String token = ClientTestBuilder.CLIENT_TOKEN;

		// Expectations
		when(mockRepository.findByToken(token)).thenReturn(Optional.empty());

		// Do test
		try {
			service.findByToken(token);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockRepository).findByToken(token);
	}

	@Test
	public void findByToken_ClientFound_ShouldReturnClient() {
		// Set up
		String token = ClientTestBuilder.CLIENT_TOKEN;
		Client client = ClientTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByToken(token)).thenReturn(Optional.of(client));

		// Do test
		Client result = service.findByToken(token);

		// Assertions
		verify(mockRepository).findByToken(token);

		assertNotNull(result);
		assertThat(result, is(client));
	}

}
