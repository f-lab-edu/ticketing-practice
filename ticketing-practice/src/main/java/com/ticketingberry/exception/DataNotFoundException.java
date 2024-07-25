package com.ticketingberry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)	// 404 상태코드
public class DataNotFoundException extends RuntimeException {
	public DataNotFoundException(String message) {
		super(message);
	}
}

