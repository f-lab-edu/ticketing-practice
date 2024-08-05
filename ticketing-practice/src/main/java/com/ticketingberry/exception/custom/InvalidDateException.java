package com.ticketingberry.exception.custom;

public class InvalidDateException extends RuntimeException {
	public InvalidDateException(String message) {
		super(message);
	}
}
