package com.tsm.sendemail.controller;

import static com.tsm.sendemail.util.ErrorCodes.ACCESS_NOT_ALLOWED;
import static com.tsm.sendemail.util.ErrorCodes.MISSING_HEADER;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.exceptions.ForbiddenRequestException;
import com.tsm.sendemail.service.AssertClientRequest;

@SuppressWarnings(value = { "rawtypes" })
public class BaseController {

    protected static final String ADMIN_TOKEN_HEADER = "AT";

    public static final String JSON_VALUE = "application/json";

    protected static final String COMMA_SEPARATOR = ",";

    @Autowired
    private Validator validator;

    @Autowired
    private AssertClientRequest assertClientRequest;

    protected <T> void validate(final T object, Class clazz) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, clazz);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    protected void assertClientHeader(HttpServletRequest request) {

        if (StringUtils.isBlank(request.getHeader(ADMIN_TOKEN_HEADER))) {
            throw new BadRequestException(MISSING_HEADER);
        }

        if (!assertClientRequest.isRequestAllowedCheckingAdminToken(request.getHeader(ADMIN_TOKEN_HEADER))) {
            throw new ForbiddenRequestException(ACCESS_NOT_ALLOWED);
        }

    }

}
