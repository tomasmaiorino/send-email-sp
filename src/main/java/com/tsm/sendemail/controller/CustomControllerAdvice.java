package com.tsm.sendemail.controller;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomControllerAdvice {

    private Function<ConstraintViolation<?>, FieldError> generateFieldErrorFunction = (c) -> {
        String field = StringUtils.substringAfter(c.getPropertyPath().toString(), ".");
        return new FieldError(field, c.getMessageTemplate(), c.getMessage());
    };

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<FieldError> handleConstraintViolationException(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream().map(generateFieldErrorFunction).collect(Collectors.toList());
    }
    // return new ResponseEntity(getErrors(ex.getConstraintViolations()), HttpStatus.BAD_REQUEST);
    // }

    // @RequestMapping(produces = "application/json")
    // @ExceptionHandler(ConstraintViolationException.class)
    // @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    // public @ResponseBody ResponseEntity<List<FieldError>> handleConstraintViolationException(
    // ConstraintViolationException ex) {
    // return new ResponseEntity(getErrors(ex.getConstraintViolations()), HttpStatus.BAD_REQUEST);
    // }
    //
    // @ExceptionHandler(IllegalArgumentException.class)
    // @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    // protected @ResponseBody ResponseEntity<Map<String, String>> handleException(final IllegalArgumentException e) {
    // Map<String, String> response = new HashMap<>();
    // response.put("message", e.getMessage());
    // return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    // }
    //
    // public static List<FieldError> getErrors(Set<ConstraintViolation<?>> constraintViolations) {
    // return constraintViolations.stream().map(FieldError::of).collect(Collectors.toList());
    // }

    public static FieldError of(ConstraintViolation<?> constraintViolation) {

        String field = StringUtils.substringAfter(constraintViolation.getPropertyPath().toString(), ".");

        return new FieldError(field, constraintViolation.getMessageTemplate(), constraintViolation.getMessage());

    }

}
