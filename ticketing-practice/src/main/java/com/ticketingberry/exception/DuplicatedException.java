package com.ticketingberry.exception;

// 409(CONFLICT)
public class DuplicatedException extends RuntimeException {
	public DuplicatedException(String message) {
		super(message);
	}
}

