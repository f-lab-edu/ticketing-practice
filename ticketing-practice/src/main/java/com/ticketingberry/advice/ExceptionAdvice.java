package com.ticketingberry.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ticketingberry.exception.DataNotFoundException;
import com.ticketingberry.exception.DuplicatedException;
import com.ticketingberry.exception.PasswordsDoNotMatchException;

@RestControllerAdvice
public class ExceptionAdvice {
	// 400(잘못된 요청): 요청의 구문이 잘못되었다.
	// 상태 코드는 PasswordsDoNotMatchException 클래스에 @ResponseStatus로 설정
	@ExceptionHandler(PasswordsDoNotMatchException.class)
	@ResponseBody
	public String PasswordsDoNotMatchExceptionHandler(PasswordsDoNotMatchException e) {
		return e.getMessage();
	}
	
	// 404(찾을 수 없음): 지정한 리소스를 찾을 수 없다.
	// 상태 코드는 DataNotFoundException 클래스에 @ResponseStatus로 설정
	@ExceptionHandler(DataNotFoundException.class)
	@ResponseBody
	public String DataNotFoundExceptionHandler(DataNotFoundException e) {
		return e.getMessage();
	}
	
	// 409(충돌): 서버가 요청을 수행하는 중에 충돌이 발생하였다.
	// 상태 코드는 DuplicatedException 클래스에 @ResponseStatus로 설정
	@ExceptionHandler(DuplicatedException.class)
	@ResponseBody
	public String DuplicatedExceptionHandler(DuplicatedException e) {
		return e.getMessage();
	}
}

