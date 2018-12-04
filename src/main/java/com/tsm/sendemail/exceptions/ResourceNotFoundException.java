package com.tsm.sendemail.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String errorCode;

    public ResourceNotFoundException(final String errorCode) {
        this.setErrorCode(errorCode);
    }

}
