package com.ticketingberry.exception;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
	// 400(잘못된 요청): 요청의 구문이 잘못되었다.
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse IllegalArgumentExceptionHandler(IllegalArgumentException e) {
		return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
	}
	
	// 404(찾을 수 없음): 지정한 리소스를 찾을 수 없다.
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse NoSuchElementExceptionHandler(NoSuchElementException e) {
		return new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
	}
	
	// 409(충돌): 서버가 요청을 수행하는 중에 충돌이 발생하였다.
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public ErrorResponse DataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
		return new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value());
	}
}
