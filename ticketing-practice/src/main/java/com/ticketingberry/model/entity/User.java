package com.ticketingberry.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Component		// 빈 등록을 빈 클래스 자체에다가 할 수 있음. 타입 기반의 자동 주입 어노테이션
public class User {		// 회원 테이블
	@Id
	// @GeneratedValue, @Column은 세트
	@GeneratedValue(strategy = GenerationType.IDENTITY)	// auto_increment
	@Column(name = "user_id", nullable = false)
	private long id;			// 회원 고유 id (1부터 자동 증가)	
	
	// 지연 로딩 => 이 테이블의 컬럼들을 불러올 때 의존 관계에 있는 테이블의 컬럼들은 불러오지 않음 (fetch = FetchType.LAZY)
	// 즉시 로딩 => 이 테이블의 컬럼들을 불러올 때 의존 관계에 있는 테이블의 컬럼들까지 같이 불러옴 (fetch = FetchType.EAGER)
	// @XXXOne(@ManyToOne, @OneToOne)은 기본값이 즉시 로딩 
	// @XXXMany(@OneToMany, @ManyToMany)는 기본값이 지연 로딩 
	@OneToOne(fetch = FetchType.LAZY)	// 지연 로딩하도록 설정
	@JoinColumn(name = "image_id")
	private Image profileImage;		// 회원 프로필 이미지
	
	@Column(unique = true, length = 20, nullable = false)		// column을 unique 키로 설정
	private String username;		// 회원 아이디(ID)
	
	@Column(nullable = false)		// 암호화된 password가 들어가므로 길이 짧게 지정 X
	private String password;		// 회원 비밀번호(PW)
	
	@Column(length = 50, nullable = false)
	private String nickname;		// 회원 닉네임
	
	@Column(length = 50, nullable = false)
	private String email;			// 회원 이메일
	
	@Column(length = 20, nullable = false)
	private String phone;			// 회원 휴대폰 번호
	
	@Column(length = 10, nullable = false)
	private String birth;			// 회원 생년월일
	
	@Column(length = 5, nullable = false)
	private String gender;			// 회원 성별
	
	@Column(length = 10, nullable = false)
	private String role;			// 회원 역할
	
	@CreationTimestamp					// default값: 현재 시간
	@Column(nullable = false)
	private LocalDateTime createdAt;	// 회원 객체 생성 시간
	
	@CreationTimestamp	
	@Column(nullable = false)
	private LocalDateTime updatedAt;	// 회원 객체 수정 시간
}
