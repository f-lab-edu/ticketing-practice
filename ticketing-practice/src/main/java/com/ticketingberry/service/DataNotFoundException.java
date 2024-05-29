package com.ticketingberry.service;

public class DataNotFoundException extends RuntimeException {
	static final long serialVersionUID = 1L;
	public DataNotFoundException() {}
	public DataNotFoundException(String message) {
		super(message);
	}
}
