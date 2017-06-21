package com.tsm.sendemail.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.springframework.test.util.ReflectionTestUtils;

import com.tsm.sendemail.resources.BaseResource;

public class BaseResourceTest {

	protected Validator validator;

	protected void checkResource(Supplier<? extends BaseResource> resource, final String attribute, final Object value,
			final String message) {
		assertResource(resource.get(), attribute, value, message);
	}

	protected <T extends BaseResource> void assertResource(final T t, final String attribute, final Object value,
			final String message) {
		ReflectionTestUtils.setField(t, attribute, value);
		// Do test
		Set<ConstraintViolation<T>> result = validator.validate(t, Default.class);

		// Assertions
		assertNotNull(result);
		if (Objects.nonNull(message)) {
			assertThat(result.size(), is(1));
			assertThat(result.iterator().next().getMessageTemplate(), is(message));
		} else {
			assertThat(result.size(), is(0));
		}

	}

}
