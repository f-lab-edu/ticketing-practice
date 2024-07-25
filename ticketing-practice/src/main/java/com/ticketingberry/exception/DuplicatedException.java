package com.ticketingberry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)	// 409 상태코드
public class DuplicatedException extends RuntimeException {
	public DuplicatedException(String message) {
		super(message);
	}
}

