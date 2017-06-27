package com.tsm.sendemail.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings(value = {"rawtypes" })
public class BaseController {
    
    public static final String JSON_VALUE = "application/json";

    @Autowired
    private Validator validator;

    protected <T> void validate(final T object, Class clazz) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, clazz);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }
}
