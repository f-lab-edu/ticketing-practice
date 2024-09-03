package com.ticketingberry.dto.user;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ticketingberry.domain.user.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class UserResponse extends UserDto {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime updatedAt;
	
	// DTO 생성 정적 팩토리 메서드
	public static UserResponse of(User user) {
		return UserResponse.builder()
				.username(user.getUsername())
				.nickname(user.getNickname())
				.email(user.getEmail())
				.phone(user.getPhone())
				.birthAt(user.getBirthAt())
				.gender(user.getGender())
				.role(user.getRole())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.build();
	}
}
