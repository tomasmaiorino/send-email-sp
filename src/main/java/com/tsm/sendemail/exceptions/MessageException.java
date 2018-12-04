package com.tsm.sendemail.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class MessageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String errorCode;

    public MessageException(final String errorCode) {
        this.setErrorCode(errorCode);
    }

}
