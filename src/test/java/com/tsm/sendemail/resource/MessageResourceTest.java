package com.tsm.sendemail.resource;

import static com.tsm.sendemail.util.ErrorCodes.INVALID_EMAIL;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_MESSAGE_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_SENDER_NAME_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_SUBJECT_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_MESSAGE;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_SENDER_EMAIL;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_SENDER_NAME;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_SUBJECT;
import static com.tsm.sendemail.util.MessageTestBuilder.LARGE_MESSAGE;
import static com.tsm.sendemail.util.MessageTestBuilder.LARGE_SENDER_EMAIL;
import static com.tsm.sendemail.util.MessageTestBuilder.LARGE_SENDER_NAME;
import static com.tsm.sendemail.util.MessageTestBuilder.LARGE_SUBJECT;
import static com.tsm.sendemail.util.MessageTestBuilder.SMALL_MESSAGE;
import static com.tsm.sendemail.util.MessageTestBuilder.SMALL_SENDER_NAME;
import static com.tsm.sendemail.util.MessageTestBuilder.SMALL_SUBJECT;

import java.util.function.Supplier;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.sendemail.resources.BaseResource;
import com.tsm.sendemail.util.MessageTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class MessageResourceTest extends BaseResourceTest {

    private Supplier<BaseResource> buildResourceFunction = MessageTestBuilder::buildResoure;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    public void build_NullMessageGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "message", null, REQUIRED_MESSAGE);
    }

    @Test
    public void build_SmallMessageGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "message", SMALL_MESSAGE, INVALID_MESSAGE_SIZE);
    }

    @Test
    public void build_LargeMessageGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "message", LARGE_MESSAGE, INVALID_MESSAGE_SIZE);
    }

    @Test
    public void build_EmptyMessageGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "message", "", INVALID_MESSAGE_SIZE);
    }
    //

    @Test
    public void build_NullSubjectGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "subject", null, REQUIRED_SUBJECT);
    }

    @Test
    public void build_SmallSubjectGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "subject", SMALL_SUBJECT, INVALID_SUBJECT_SIZE);
    }

    @Test
    public void build_LargeSubjectGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "subject", LARGE_SUBJECT, INVALID_SUBJECT_SIZE);
    }

    @Test
    public void build_EmptySubjectGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "subject", "", INVALID_SUBJECT_SIZE);
    }
    //

    @Test
    public void build_NullSenderNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "senderName", null, REQUIRED_SENDER_NAME);
    }

    @Test
    public void build_SmallSenderNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "senderName", SMALL_SENDER_NAME, INVALID_SENDER_NAME_SIZE);
    }

    @Test
    public void build_LargeSenderNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "senderName", LARGE_SENDER_NAME, INVALID_SENDER_NAME_SIZE);
    }

    @Test
    public void build_EmptySenderNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "senderName", "", INVALID_SENDER_NAME_SIZE);
    }

    //

    @Test
    public void build_NullSenderEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "senderEmail", null, REQUIRED_SENDER_EMAIL);
    }

    @Test
    public void build_LargeSenderEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "senderEmail", LARGE_SENDER_EMAIL, INVALID_EMAIL);
    }

    @Test
    public void build_EmptySenderEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "senderEmail", "", INVALID_EMAIL);
    }

}
