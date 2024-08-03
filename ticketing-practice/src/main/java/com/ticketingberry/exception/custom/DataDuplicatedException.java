package com.ticketingberry.exception.custom;

public class DataDuplicatedException extends RuntimeException {
	public DataDuplicatedException(String message) {
		super(message);
	}
}
