package com.tsm.sendemail.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

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

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.exceptions.ResourceNotFoundException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.repository.ClientRepository;
import com.tsm.sendemail.util.ClientTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ClientServiceTest {

    @InjectMocks
    private ClientService service;

    @Mock
    private ClientRepository repository;

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
        verifyZeroInteractions(repository);
    }

    @Test
    public void save_DuplicatedTokenGiven_ShouldThrowException() {
        // Set up
        Client client = ClientTestBuilder.buildModel();

        // Expectations
        when(repository.findByToken(client.getToken())).thenReturn(Optional.of(client));

        // Do test
        try {
            service.save(client);
            fail();
        } catch (BadRequestException e) {
        }

        // Assertions
        verify(repository).findByToken(client.getToken());
        verify(repository, times(0)).save(client);
    }

    @Test
    public void save_ValidClientGiven_ShouldCreateClient() {
        // Set up
        Client client = ClientTestBuilder.buildModel();

        // Expectations
        when(repository.save(client)).thenReturn(client);
        when(repository.findByToken(client.getToken())).thenReturn(Optional.empty());

        // Do test
        Client result = service.save(client);

        // Assertions
        verify(repository).save(client);
        verify(repository).findByToken(client.getToken());
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
        verifyZeroInteractions(repository);
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
        verifyZeroInteractions(repository);
    }

    @Test
    public void findByToken_ClientNotFound_ShouldThrowException() {
        // Set up
        String token = ClientTestBuilder.CLIENT_TOKEN;

        // Expectations
        when(repository.findByToken(token)).thenReturn(Optional.empty());

        // Do test
        try {
            service.findByToken(token);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(repository).findByToken(token);
    }

    @Test
    public void findByToken_ClientFound_ShouldReturnClient() {
        // Set up
        String token = ClientTestBuilder.CLIENT_TOKEN;
        Client client = ClientTestBuilder.buildModel();

        // Expectations
        when(repository.findByToken(token)).thenReturn(Optional.of(client));

        // Do test
        Client result = service.findByToken(token);

        // Assertions
        verify(repository).findByToken(token);

        assertNotNull(result);
        assertThat(result, is(client));
    }

    @Test
    public void findByStatus_NullClientStatusGiven_ShouldThrowException() {
        // Set up
        ClientStatus status = null;

        // Do test
        try {
            service.findByStatus(status);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(repository);
    }

    @Test
    public void findByStatus_NotClientsFoundGiven_ShouldReturnEmptySet() {
        // Set up
        ClientStatus status = ClientStatus.ACTIVE;

        // Expectations
        when(service.findByStatus(status)).thenReturn(Collections.emptySet());

        // Do test
        Set<Client> result = service.findByStatus(status);

        // Assertions
        verify(repository).findByStatus(status);

        assertNotNull(result);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void findByStatus_ClientsFoundGiven_ShouldReturnContent() {
        // Set up
        ClientStatus status = ClientStatus.ACTIVE;
        Client client = ClientTestBuilder.buildModel();

        // Expectations
        when(service.findByStatus(status)).thenReturn(Collections.singleton(client));

        // Do test
        Set<Client> result = service.findByStatus(status);

        // Assertions
        verify(repository).findByStatus(status);

        assertNotNull(result);
        assertThat(result.isEmpty(), is(false));
        assertThat(result.contains(client), is(true));
    }

    @Test
    public void findByIsAdmin_NullAdminGiven_ShouldThrowException() {
        // Set up
        Boolean isAdmin = null;

        // Do test
        try {
            service.findByIsAdmin(isAdmin);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(repository);
    }

    @Test
    public void findByIsAdmin_NotClientsFoundGiven_ShouldReturnEmptySet() {
        // Set up
        Boolean isAdmin = true;

        // Expectations
        when(service.findByIsAdmin(isAdmin)).thenReturn(Collections.emptySet());

        // Do test
        Set<Client> result = service.findByIsAdmin(isAdmin);

        // Assertions
        verify(repository).findByIsAdmin(isAdmin);

        assertNotNull(result);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void findByIsAdmin_ClientsFoundGiven_ShouldReturnContent() {
        // Set up
        Boolean isAdmin = true;
        Client client = ClientTestBuilder.buildModel();

        // Expectations
        when(service.findByIsAdmin(isAdmin)).thenReturn(Collections.singleton(client));

        // Do test
        Set<Client> result = service.findByIsAdmin(isAdmin);

        // Assertions
        verify(repository).findByIsAdmin(isAdmin);

        assertNotNull(result);
        assertThat(result.isEmpty(), is(false));
        assertThat(result.contains(client), is(true));
    }

}
