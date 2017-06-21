package com.tsm.sendemail.controller;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.validation.ValidationException;
import javax.validation.Validator;

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

import com.tsm.sendemail.resources.ClientResource;
import com.tsm.sendemail.service.ClientService;
import com.tsm.sendemail.util.ClientTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ClientsControllerTest {

	@Mock
	private ClientService mockService;

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
	public void save_InvalidProductVariantResourceGiven_ShouldThrowException() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();

		// Expectations
		when(validator.validate(resource, ClientResource.class)).thenThrow(new ValidationException());

		// Do test
		try {
			controller.save(resource);
			fail();
		} catch (ValidationException e) {
		}

		// Assertions
		verifyZeroInteractions(mockService);
	}
}
