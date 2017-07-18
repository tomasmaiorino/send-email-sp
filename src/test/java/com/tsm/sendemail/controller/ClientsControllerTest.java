package com.tsm.sendemail.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.exceptions.ForbiddenRequestException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.parser.ClientParser;
import com.tsm.sendemail.resources.ClientResource;
import com.tsm.sendemail.service.AssertClientRequest;
import com.tsm.sendemail.service.ClientService;
import com.tsm.sendemail.util.ClientTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ClientsControllerTest {

	private static final String ADMIN_TOKEN_HEADER = "AT";

	@Mock
	private ClientService mockService;

	@Mock
	private ClientParser mockParser;

	@InjectMocks
	private ClientsController controller;

	@Mock
	private AssertClientRequest mockAssertClientRequest;

	@Mock
	private Validator validator;

	@Mock
	private MockHttpServletRequest request;

	private static final String ADMIN_TOKEN_VALUE = "qwerty";

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(request.getHeader(ADMIN_TOKEN_HEADER)).thenReturn(ADMIN_TOKEN_VALUE);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		when(mockAssertClientRequest.isRequestAllowedCheckingAdminToken(ADMIN_TOKEN_VALUE)).thenReturn(true);
	}

	@Test
	public void save_NullHeaderGiven_ShouldThrowException() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();

		// Expectations
		when(request.getHeader(ADMIN_TOKEN_HEADER)).thenReturn(null);

		// Do test
		try {
			controller.save(resource, request);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verifyZeroInteractions(mockService, mockParser, validator, mockAssertClientRequest);
	}

	@Test
	public void save_EmptyHeaderGiven_ShouldThrowException() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();

		// Expectations
		when(request.getHeader(ADMIN_TOKEN_HEADER)).thenReturn("");

		// Do test
		try {
			controller.save(resource, request);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verifyZeroInteractions(mockService, mockParser, validator, mockAssertClientRequest);
	}

	@Test
	public void save_InvalidHeaderGiven_ShouldThrowException() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();

		// Expectations
		when(mockAssertClientRequest.isRequestAllowedCheckingAdminToken(ADMIN_TOKEN_VALUE)).thenReturn(false);

		// Do test
		try {
			controller.save(resource, request);
			fail();
		} catch (ForbiddenRequestException e) {
		}

		// Assertions
		verify(mockAssertClientRequest).isRequestAllowedCheckingAdminToken(ADMIN_TOKEN_VALUE);
		verifyZeroInteractions(mockService, mockParser, validator);
	}

	@Test
	public void save_InvalidClientResourceGiven_ShouldThrowException() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();

		// Expectations
		when(validator.validate(resource, Default.class)).thenThrow(new ValidationException());

		// Do test
		try {
			controller.save(resource, request);
			fail();
		} catch (ValidationException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verifyZeroInteractions(mockService, mockParser);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void save_DuplicatedClientResourceGiven_ShouldSaveClient() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();
		Client client = ClientTestBuilder.buildModel();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockParser.toModel(resource)).thenReturn(client);
		when(mockService.save(client)).thenThrow(BadRequestException.class);

		// Do test
		try {
			controller.save(resource, request);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).save(client);
		verify(mockParser).toModel(resource);

	}

	@Test
	public void save_ValidClientResourceGiven_ShouldSaveClient() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();
		Client client = ClientTestBuilder.buildModel();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockParser.toModel(resource)).thenReturn(client);
		when(mockService.save(client)).thenReturn(client);
		when(mockParser.toResource(client)).thenReturn(resource);

		// Do test
		ClientResource result = controller.save(resource, request);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).save(client);
		verify(mockParser).toModel(resource);
		verify(mockParser).toResource(client);

		assertNotNull(result);
		assertThat(result, is(resource));
	}
}
