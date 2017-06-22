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

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.parser.ClientParser;
import com.tsm.sendemail.resources.ClientResource;
import com.tsm.sendemail.service.ClientService;
import com.tsm.sendemail.util.ClientTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ClientsControllerTest {

	@Mock
	private ClientService mockService;

	@Mock
	private ClientParser mockParser;

	@InjectMocks
	private ClientsController controller;

	@Mock
	private Validator validator;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	public void save_InvalidClientResourceGiven_ShouldThrowException() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();

		// Expectations
		when(validator.validate(resource, Default.class)).thenThrow(new ValidationException());

		// Do test
		try {
			controller.save(resource);
			fail();
		} catch (ValidationException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verifyZeroInteractions(mockService, mockParser);
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
		ClientResource result = controller.save(resource);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).save(client);
		verify(mockParser).toModel(resource);
		verify(mockService).save(client);
		verify(mockParser).toResource(client);

		assertNotNull(result);
		assertThat(result, is(resource));
	}
}
