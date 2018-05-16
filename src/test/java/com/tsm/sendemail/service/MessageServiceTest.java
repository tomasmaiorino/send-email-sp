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

import com.tsm.sendemail.repository.MessageRepositoryImpl;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.sendemail.exceptions.ResourceNotFoundException;
import com.tsm.sendemail.model.Client;
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
    private MessageRepositoryImpl repository;

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
        verifyZeroInteractions(repository);
    }

    @Test
    public void save_ValidMessageGiven_ShouldCreateMessage() {
        // Set up
        Message message = MessageTestBuilder.buildModel();

        // Expectations
        when(repository.save(message)).thenReturn(message);

        // Do test
        Message result = service.save(message);

        // Assertions
        verify(repository).save(message);
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
        verifyNoMoreInteractions(repository);
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
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByIdAndClient_NotFoundMessageGiven_ShouldThrowException() {
        // Set up
        Long id = 1l;
        Client client = ClientTestBuilder.buildModel();

        // Expectations
        when(repository.findByIdAndClient(id, client)).thenReturn(Optional.empty());

        // Do test
        try {
            service.findByIdAndClient(id, client);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(repository).findByIdAndClient(id, client);
    }

    @Test
    public void findByIdAndClient_FoundMessageGiven_ShouldReturnMessage() {
        // Set up
        Long id = 1l;
        Client client = ClientTestBuilder.buildModel();
        Message message = MessageTestBuilder.buildModel();

        // Expectations
        when(repository.findByIdAndClient(id, client)).thenReturn(Optional.of(message));

        // Do test
        Message result = service.findByIdAndClient(id, client);

        // Assertions
        verify(repository).findByIdAndClient(id, client);

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
    public void findByClientAndCreatedBetween_NullClientStatusGiven_ShouldThrowException() {
        // Set up
        Client client = null;
        LocalDateTime initialDate = LocalDateTime.now();
        LocalDateTime finalDate = LocalDateTime.now();

        // Do test
        try {
            service.findByClientAndCreatedBetween(client, initialDate, finalDate);

            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByClientAndCreatedBetween_NullInitialDateGiven_ShouldThrowException() {
        // Set up
        Client client = ClientTestBuilder.buildModel();
        LocalDateTime initialDate = null;
        LocalDateTime finalDate = LocalDateTime.now();

        // Do test
        try {
            service.findByClientAndCreatedBetween(client, initialDate, finalDate);

            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByClientAndCreatedBetween_NullFinalDateGiven_ShouldThrowException() {
        // Set up
        Client client = ClientTestBuilder.buildModel();
        LocalDateTime initialDate = LocalDateTime.now();
        LocalDateTime finalDate = null;

        // Do test
        try {
            service.findByClientAndCreatedBetween(client, initialDate, finalDate);

            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByClientAndCreatedBetween_NotFoundMessagesGiven_ShouldEmptyContent() {
        // Set up
        Client client = ClientTestBuilder.buildModel();
        LocalDateTime initialDate = LocalDateTime.now();
        LocalDateTime finalDate = LocalDateTime.now();

        // Expectations
        when(repository.findByClientAndCreatedBetween(client, initialDate, finalDate))
            .thenReturn(Collections.emptySet());

        // Do test
        Set<Message> result = service.findByClientAndCreatedBetween(client, initialDate, finalDate);

        // Assertions
        verify(repository).findByClientAndCreatedBetween(client, initialDate, finalDate);

        assertNotNull(result);
        assertThat(result.isEmpty(), is(true));
    }

    public void findByClientAndCreatedBetween_FounddMessagesGiven_ShouldContent() {
        // Set up
        Client client = ClientTestBuilder.buildModel();
        LocalDateTime initialDate = LocalDateTime.now();
        LocalDateTime finalDate = LocalDateTime.now();
        Message message = MessageTestBuilder.buildModel();

        // Expectations
        when(repository.findByClientAndCreatedBetween(client, initialDate, finalDate))
            .thenReturn(Collections.singleton(message));

        // Do test
        Set<Message> result = service.findByClientAndCreatedBetween(client, initialDate, finalDate);

        // Assertions
        verify(repository).findByClientAndCreatedBetween(client, initialDate, finalDate);

        assertNotNull(result);
        assertThat(result.isEmpty(), is(false));
        assertThat(result.contains(message), is(true));
    }
}
