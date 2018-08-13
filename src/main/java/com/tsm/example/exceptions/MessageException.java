package com.tsm.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class MessageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String errorCode;

	public MessageException(final String errorCode) {
		this.setErrorCode(errorCode);
	}

	public MessageException(final String errorCode, String message) {
		super(message);
	}

}
