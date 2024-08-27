package com.ticketingberry.dto.user;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ticketingberry.domain.user.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest extends UserDto {
	@NotNull(message = "비밀번호를 입력해주세요.")
	@Size(min = 7, max = 20, message = "비밀번호를 7 ~ 20자 사이로 입력해주세요.")
	private String password1;
	
	@NotNull(message = "비밀번호 확인을 입력해주세요.")
	@Size(min = 7, max = 20, message = "비밀번호 확인을 7 ~ 20자 사이로 입력해주세요.")
	private String password2;	// password1과 password2 일치 체크
	
	// Entity 생성 정적 팩토리 메서드
	public static User newUser(UserCreateRequest userCreateRequest, PasswordEncoder passwordEncoder) {
		return User.builder()
				.username(userCreateRequest.getUsername())
				.password(passwordEncoder.encode(userCreateRequest.getPassword1()))
				.nickname(userCreateRequest.getNickname())
				.email(userCreateRequest.getEmail())
				.phone(userCreateRequest.getPhone())
				.birth(userCreateRequest.getBirth())
				.gender(userCreateRequest.getGender())
				.role(userCreateRequest.getRole())
				.build();
	}
}
