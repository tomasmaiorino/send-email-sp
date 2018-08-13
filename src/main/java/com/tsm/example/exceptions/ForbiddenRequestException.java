package com.tsm.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String errorCode;

	public ForbiddenRequestException(final String errorCode) {
		this.errorCode = errorCode;
	}

}
