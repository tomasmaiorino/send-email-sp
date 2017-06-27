package com.tsm.sendemail.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
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

import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.repository.MessageRepository;
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
}
