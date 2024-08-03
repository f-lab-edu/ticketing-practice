package com.ticketingberry.exception.custom;

public class DataNotFoundException extends RuntimeException {
	public DataNotFoundException(String message) {
		super(message);
	}
}
