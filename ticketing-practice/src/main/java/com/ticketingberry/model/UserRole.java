package com.ticketingberry.model;

import lombok.Getter;

@Getter
public enum UserRole {
	// ROLE_로 시작해야 함. 그렇지 않으면 user 권한을 핸들링할 때 에러 발생
	ADMIN("ROLE_ADMIN"),	// 관리자
	USER("ROLE_USER");		// 일반 사용자
	
	private String value;
	
	UserRole(String value) {
		this.value = value;
	}
}
