package com.ticketingberry.exception.custom;

public class PasswordsUnequalException extends RuntimeException {
	public PasswordsUnequalException(String message) {
		super(message);
	}
}
