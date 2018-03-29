package com.tsm.sendemail.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.model.ClientHosts;
import com.tsm.sendemail.repository.ClientHostsRepository;
import com.tsm.sendemail.util.ClientTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ClientHostsServiceTest {

    @InjectMocks
    private ClientHostsService service;

    @Mock
    private ClientHostsRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByClientStatus_EmptyClientTokenGiven_ShouldThrowException() {
        // Set up
        ClientStatus status = null;

        // Do test
        try {
            service.findByClientStatus(status);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(repository);
    }

    @Test
    public void findByToken_ClientHostsNotFound_ShouldThrowException() {
        // Set up
        ClientStatus status = ClientStatus.ACTIVE;

        // Expectations
        when(repository.findByClientStatus(status)).thenReturn(Collections.emptySet());

        // Do test
        Set<ClientHosts> result = service.findByClientStatus(status);

        // Assertions
        verify(repository).findByClientStatus(status);

        assertNotNull(result);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void findByToken_ClientFound_ShouldReturnClient() {
        // Set up
        ClientStatus status = ClientStatus.ACTIVE;
        Set<ClientHosts> clientHosts = ClientTestBuilder.buildClientHost();

        // Expectations
        when(repository.findByClientStatus(status)).thenReturn(clientHosts);

        // Do test
        Set<ClientHosts> result = service.findByClientStatus(status);

        // Assertions
        verify(repository).findByClientStatus(status);

        assertNotNull(result);
        assertThat(result.isEmpty(), is(false));
        assertThat(result.size(), is(clientHosts.size()));
    }

}
