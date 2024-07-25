package com.ticketingberry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)	// 400 상태코드
public class PasswordsDoNotMatchException extends RuntimeException {
	public PasswordsDoNotMatchException(String message) {
		super(message);
	}
}
