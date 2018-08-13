package com.tsm.example.resource;

import static com.tsm.example.util.ClientTestBuilder.LARGE_NAME;
import static com.tsm.example.util.ClientTestBuilder.LARGE_TOKEN;
import static com.tsm.example.util.ClientTestBuilder.RESOURCE_INVALID_EMAIL;
import static com.tsm.example.util.ClientTestBuilder.SMALL_NAME;
import static com.tsm.example.util.ClientTestBuilder.SMALL_TOKEN;
import static com.tsm.example.util.ErrorCodes.INVALID_EMAIL;
import static com.tsm.example.util.ErrorCodes.INVALID_NAME_SIZE;
import static com.tsm.example.util.ErrorCodes.INVALID_TOKEN_SIZE;
import static com.tsm.example.util.ErrorCodes.REQUIRED_EMAIL;
import static com.tsm.example.util.ErrorCodes.REQUIRED_EMAIL_RECIPIENT;
import static com.tsm.example.util.ErrorCodes.REQUIRED_HOSTS;
import static com.tsm.example.util.ErrorCodes.REQUIRED_NAME;
import static com.tsm.example.util.ErrorCodes.REQUIRED_TOKEN;

import java.util.Collections;
import java.util.function.Supplier;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.example.resources.BaseResource;
import com.tsm.example.util.ClientTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ClientResourceTest extends BaseResourceTest {

    private Supplier<BaseResource> buildResourceFunction = ClientTestBuilder::buildResoure;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    public void build_NullNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", null, REQUIRED_NAME);
    }

    @Test
    public void build_SmallNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", SMALL_NAME, INVALID_NAME_SIZE);
    }

    @Test
    public void build_LargeNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", LARGE_NAME, INVALID_NAME_SIZE);
    }

    @Test
    public void build_EmptyNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", "", INVALID_NAME_SIZE);
    }

    //

    @Test
    public void build_NullTokenGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "token", null, REQUIRED_TOKEN);
    }

    @Test
    public void build_SmallTokenGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "token", SMALL_TOKEN, INVALID_TOKEN_SIZE);
    }

    @Test
    public void build_LargeTokenGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "token", LARGE_TOKEN, INVALID_TOKEN_SIZE);
    }

    @Test
    public void build_EmptyTokenGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "token", "", INVALID_TOKEN_SIZE);
    }

    //

    @Test
    public void build_NullEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "email", null, REQUIRED_EMAIL);
    }

    @Test
    public void build_InvalidEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "email", RESOURCE_INVALID_EMAIL, INVALID_EMAIL);
    }

    @Test
    public void build_EmptyEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "email", "", REQUIRED_EMAIL);
    }

    @Test
    public void build_NullEmailRecipientGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "emailRecipient", null, REQUIRED_EMAIL_RECIPIENT);
    }

    @Test
    public void build_InvalidEmailRecipientGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "emailRecipient", RESOURCE_INVALID_EMAIL, INVALID_EMAIL);
    }

    @Test
    public void build_EmptyEmailRecipientGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "emailRecipient", "", REQUIRED_EMAIL_RECIPIENT);
    }

    //

    @Test
    public void build_NullHostsGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "hosts", null, REQUIRED_HOSTS);
    }

    @Test
    public void build_EmptyHostsGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "hosts", Collections.emptySet(), REQUIRED_HOSTS);
    }

    // TODO validate hosts content
}
