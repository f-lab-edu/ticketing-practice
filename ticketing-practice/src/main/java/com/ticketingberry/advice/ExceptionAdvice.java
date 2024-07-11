package com.ticketingberry.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ticketingberry.exception.DataNotFoundException;
import com.ticketingberry.exception.DuplicatedExcpetion;

@RestControllerAdvice
public class ExceptionAdvice {
	// 400(잘못된 요청): 요청의 구문이 잘못되었다.
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> IllegalArgumentExceptionHandler(IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	// 404(찾을 수 없음): 지정한 리소스를 찾을 수 없다.
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<String> DataNotFoundExceptionHandler(DataNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	// 409(충돌): 서버가 요청을 수행하는 중에 충돌이 발생하였다.
	@ExceptionHandler(DuplicatedExcpetion.class)
	public ResponseEntity<String> DuplicatedExceptionHandler(DuplicatedExcpetion e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
}

