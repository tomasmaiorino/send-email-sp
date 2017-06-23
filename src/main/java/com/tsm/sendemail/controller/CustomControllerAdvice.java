package com.tsm.sendemail.controller;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import exception.FieldError;

@ControllerAdvice
public class CustomControllerAdvice {

    @Autowired
    private MessageSource messageSource;

    private Function<ConstraintViolation<?>, FieldError> generateFieldErrorFunction = (c) -> {
        String errorMessage = resolveLocalizedMessage(c.getMessageTemplate());
        return new FieldError(errorMessage, c.getPropertyPath().toString());
    };

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ResponseEntity<List<FieldError>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<FieldError> response = ex.getConstraintViolations().stream().map(generateFieldErrorFunction).collect(Collectors.toList());
        return new ResponseEntity<List<FieldError>>(response, HttpStatus.BAD_REQUEST);
    }

    private String resolveLocalizedMessage(final String key, final Object... args) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, "General Error", currentLocale);
    }
}
