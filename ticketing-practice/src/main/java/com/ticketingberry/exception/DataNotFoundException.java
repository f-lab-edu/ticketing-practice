package com.ticketingberry.exception;

// 404(NOT_FOUND)
public class DataNotFoundException extends RuntimeException {
	public DataNotFoundException(String message) {
		super(message);
	}
}

