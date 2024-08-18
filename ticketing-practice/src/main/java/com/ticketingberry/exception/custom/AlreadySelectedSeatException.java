package com.ticketingberry.exception.custom;

public class AlreadySelectedSeatException extends RuntimeException {
	public AlreadySelectedSeatException(String message) {
		super(message);
	}
}
