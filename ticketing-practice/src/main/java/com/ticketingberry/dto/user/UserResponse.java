package com.ticketingberry.dto.user;

import com.ticketingberry.domain.user.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class UserResponse extends UserDto {
	// DTO 생성 정적 팩토리 메서드
	public static UserResponse of(User user) {
		return UserResponse.builder()
				.username(user.getUsername())
				.nickname(user.getNickname())
				.email(user.getEmail())
				.phone(user.getPhone())
				.birth(user.getBirth())
				.gender(user.getGender())
				.role(user.getRole())
				.build();
	}
}
