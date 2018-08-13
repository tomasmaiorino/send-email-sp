package com.tsm.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String errorCode;

	public BadRequestException(final String errorCode) {
		this.errorCode = errorCode;
	}

}
