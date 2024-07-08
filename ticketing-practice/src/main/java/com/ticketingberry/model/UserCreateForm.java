package com.ticketingberry.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
	@NotNull(message = "사용자 아이디를 입력해주세요.")
	@Size(min = 7, max = 20, message = "사용자 아이디를 7 ~ 20자 사이로 입력해주세요.")
	private String username;	// 중복 체크 -> UserController의 join 함수에서 함
	
	@NotNull(message = "비밀번호를 입력해주세요.")
	@Size(min = 7, max = 20, message = "비밀번호를 7 ~ 20자 사이로 입력해주세요.")
	private String password1;
	
	@NotNull(message = "비밀번호 확인을 입력해주세요.")
	@Size(min = 7, max = 20, message = "비밀번호 확인을 7 ~ 20자 사이로 입력해주세요.")
	private String password2;	// password1과 password2 일치 체크 -> UserController의 join 함수에서 함
	
	@NotNull(message = "닉네임을 입력해주세요.")
	@Size(min = 1, max = 25, message = "닉네임을 1 ~ 8자 사이로 입력해주세요.")
	private String nickname;

	@NotNull(message = "이메일을 입력해주세요.")
	@Email(message = "유효한 이메일 주소를 입력해주세요.")
	private String email;
	
	@NotNull(message = "전화번호를 입력해주세요.")
	@Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})\\d{4}$", message = "유효한 휴대폰 번호를 입력해주세요.")
	private String phone;
	
	@NotNull(message = "생년월일을 입력해주세요.")
	@Pattern(regexp = "^\\d{8}$", message = "생년월일은 yyyyMMdd 형식으로 입력해주세요.")
	private String birth;
	
	@NotNull(message = "성별을 입력해주세요.")
	private String gender;
	
	private String role = "USER";	// 회원가입하는 모든 사람들은 회원 권한을 가짐
}
