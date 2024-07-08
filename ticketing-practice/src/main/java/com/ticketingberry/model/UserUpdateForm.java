package com.ticketingberry.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateForm {	
	@NotNull(message = "닉네임을 입력해주세요.")
	@Size(min = 1, max = 25, message = "닉네임을 1 ~ 8자 사이로 입력해주세요.")
	private String nickname;

	@NotNull(message = "이메일을 입력해주세요.")
	@Email(message = "유효한 이메일 주소를 입력해주세요.")
	private String email;
	
	@NotNull(message = "전화번호를 입력해주세요.")
	@Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})\\d{4}$", message = "유효한 휴대폰 번호를 입력해주세요.")
	private String phone;
}
