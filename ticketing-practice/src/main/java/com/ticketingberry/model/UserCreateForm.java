package com.ticketingberry.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
	@Size(min = 7, max = 20, message = "사용자 아이디를 7 ~ 20자 사이로 입력해주세요.")
	private String username;	// 중복 체크 -> UserController의 join 함수에서 함
	
	@Size(min = 7, max = 20, message = "비밀번호를 7 ~ 20자 사이로 입력해주세요.")
	private String password1;
	
	private String password2;	// password1과 password2 일치 체크 -> UserController의 join 함수에서 함
	
	private String name;

	private String email;
	
	private String phone;
	
	private String birth;
	
	private String gender;
	
	private String role = "회원";
}


