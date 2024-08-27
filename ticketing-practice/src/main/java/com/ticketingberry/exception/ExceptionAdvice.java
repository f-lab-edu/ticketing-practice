package com.ticketingberry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ticketingberry.exception.custom.AlreadySelectedSeatException;
import com.ticketingberry.exception.custom.DataDuplicatedException;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.exception.custom.InvalidDateException;
import com.ticketingberry.exception.custom.PasswordsUnequalException;

@RestControllerAdvice
public class ExceptionAdvice {
	// 400(잘못된 요청): 요청의 구문이 잘못되었다.
	@ExceptionHandler(PasswordsUnequalException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse PasswordsUnequalExceptionHandler(PasswordsUnequalException e) {
		return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
	}
	
	// 404(찾을 수 없음): 지정한 리소스를 찾을 수 없다.
	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse DataNotFoundExceptionHandler(DataNotFoundException e) {
		return new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
	}
	
	// 409(충돌): 서버가 요청을 수행하는 중에 충돌이 발생하였다.
	@ExceptionHandler(DataDuplicatedException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public ErrorResponse DataDuplicatedExceptionHandler(DataDuplicatedException e) {
		return new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value());
	}
	
	// 400(잘못된 요청): 요청의 구문이 잘못되었다.
	@ExceptionHandler(InvalidDateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse InvalidDateExceptionHandler(InvalidDateException e) {
		return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
	}
	
	// 409(충돌): 서버가 요청을 수행하는 중에 충돌이 발생하였다.
	@ExceptionHandler(AlreadySelectedSeatException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public ErrorResponse AlreadySelectedSeatExceptionHanlder(AlreadySelectedSeatException e) {
		return new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value());
	}
}
