package com.tsm.sendemail.controller;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import exception.FieldError;

@ControllerAdvice
public class CustomControllerAdvice {

	private Function<ConstraintViolation<?>, FieldError> generateFieldErrorFunction = (c) -> {
		return new FieldError(c.getPropertyPath().toString(), c.getMessageTemplate());
	};

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public List<FieldError> handleConstraintViolationException(ConstraintViolationException ex) {
		return ex.getConstraintViolations().stream().map(generateFieldErrorFunction).collect(Collectors.toList());
	}

	public static FieldError of(ConstraintViolation<?> constraintViolation) {
		String field = StringUtils.substringAfter(constraintViolation.getPropertyPath().toString(), ".");
		return new FieldError(field, constraintViolation.getMessageTemplate());
	}
}
