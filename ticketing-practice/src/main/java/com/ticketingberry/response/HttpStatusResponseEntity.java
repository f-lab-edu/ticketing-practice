package com.ticketingberry.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpStatusResponseEntity {
	// 200(성공): 서버가 요청을 성공적으로 처리하였다.
	public static final ResponseEntity<HttpStatus> RESPONSE_OK = ResponseEntity.status(HttpStatus.OK).build();
	// 201(생성됨): 요청이 처리되어서 새로운 리소스가 생성되었다.
	public static final ResponseEntity<HttpStatus> RESPONSE_CREATED = ResponseEntity.status(HttpStatus.CREATED).build();
	// 204(콘텐츠 없음): 처리를 성공하였지만, 클라이언트에게 돌려줄 콘텐츠가 없다.
	public static final ResponseEntity<HttpStatus> RESOONSE_NO_CONTENT = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	// 400(잘못된 요청): 요청의 구문이 잘못되었다.
	public static final ResponseEntity<HttpStatus> RESPONSE_BAD_REQUEST = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	// 401(권한 없음): 지정한 리소스에 대한 액세스 권한이 없다.
	public static final ResponseEntity<HttpStatus> RESPONSE_UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	// 403(금지됨): 지정한 리소스에 대한 액세스가 금지되었다.
	public static final ResponseEntity<HttpStatus> RESPONSE_FORBIDDEN = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	// 404(찾을 수 없음): 지정한 리소스를 찾을 수 없다.
	public static final ResponseEntity<HttpStatus> RESPONSE_NOT_FOUND = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	// 409(충돌): 서버가 요청을 수행하는 중에 충돌이 발생하였다.
	public static final ResponseEntity<HttpStatus> RESPONSE_CONFLICT = ResponseEntity.status(HttpStatus.CONFLICT).build();
}
