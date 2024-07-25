package com.ticketingberry.exception;

// 400(BAD_REQUEST)
public class PasswordsDoNotMatchException extends RuntimeException {
	public PasswordsDoNotMatchException(String message) {
		super(message);
	}
}
