package com.ticketingberry.dto.user;

import static com.ticketingberry.domain.user.UserRole.USER;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ticketingberry.domain.user.Gender;
import com.ticketingberry.domain.user.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserDto {
	@NotNull(message = "사용자 아이디를 입력해주세요.")
	@Size(min = 7, max = 20, message = "사용자 아이디를 7 ~ 20자 사이로 입력해주세요.")
	private String username;
	
	@NotNull(message = "닉네임을 입력해주세요.")
	@Size(min = 1, max = 25, message = "닉네임을 1 ~ 8자 사이로 입력해주세요.")
	private String nickname;
	
	@NotNull(message = "이메일을 입력해주세요.")
	@Email(message = "유효한 이메일 주소를 입력해주세요.")
	private String email;
	
	@NotNull(message = "전화번호를 입력해주세요.")
	@Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})\\d{4}$", message = "유효한 휴대폰 번호를 입력해주세요.")
	private String phone;
	
	@NotNull(message = "생년월일을 선택해주세요.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
	private LocalDate birthAt;
	
	@NotNull(message = "성별을 선택해주세요.")
	private Gender gender;
	
	@Builder.Default
	private UserRole role = USER;
}
