package com.tsm.sendemail.model;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.sendemail.model.Message.MessageStatus;
import com.tsm.sendemail.util.ClientTestBuilder;
import com.tsm.sendemail.util.MessageTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class MessageTest {

    private String message;
    private String subject;
    private String senderName;
    private String senderEmail;
    private Client client;
    private MessageStatus status;

    @Before
    public void setUp() {
        message = random(300, true, true);
        subject = random(30, true, true);
        senderEmail = random(30, true, true);
        senderName = random(20, true, true);
        subject = random(30, true, true);
        status = MessageStatus.CREATED;
        client = ClientTestBuilder.buildModel();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullMessageGiven_ShouldThrowException() {

        // Set up
        message = null;

        // Do test
        buildMessage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_EmptyMessageGiven_ShouldThrowException() {

        // Set up
        message = "";

        // Do test
        buildMessage();
    }

    @Test
    public void build_MessageSizeGreaterGiven_ShouldThrowException() {

        // Set up
        message = random(Message.MESSAGE_MAX_LENGTH + 2, true, true);

        // Do test
        Message message = buildMessage();

        // Assertions
        assertThat(message.getMessage().length() == Message.MESSAGE_MAX_LENGTH, is(true));
        assertThat(message.getMessage().contains("..."), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullSenderNameGiven_ShouldThrowException() {

        // Set up
        senderName = null;

        // Do test
        buildMessage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_EmptySenderNameGiven_ShouldThrowException() {

        // Set up
        senderName = "";

        // Do test
        buildMessage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullSenderEmailNameGiven_ShouldThrowException() {

        // Set up
        senderEmail = null;

        // Do test
        buildMessage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_EmptySenderEmailGiven_ShouldThrowException() {

        // Set up
        senderEmail = "";

        // Do test
        buildMessage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullSubjectGiven_ShouldThrowException() {

        // Set up
        subject = null;

        // Do test
        buildMessage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_EmptySubjectGiven_ShouldThrowException() {

        // Set up
        subject = "";

        // Do test
        buildMessage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullClientGiven_ShouldThrowException() {

        // Set up
        client = null;

        // Do test
        buildMessage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullStatusGiven_ShouldThrowException() {

        // Set up
        status = null;

        // Do test
        buildMessage();
    }

    @Test
    public void build_RequiredValuesGiven_AllValuesShouldNotBeEmpty() {

        // Do test
        Message result = buildMessage();

        // Assertions
        Assert.assertThat(result, allOf(
            hasProperty("message", is(message)),
            hasProperty("subject", is(subject)),
            hasProperty("senderName", is(senderName)),
            hasProperty("senderEmail", is(senderEmail)),
            hasProperty("client", is(client))));
    }

    private Message buildMessage() {
        return MessageTestBuilder.buildModel(message, senderEmail, senderName, subject, client, status);
    }

}
