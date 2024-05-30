package com.ticketingberry.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Component
public class User {
	@Id
	// @GeneratedValue, @Column은 세트
	@GeneratedValue(strategy = GenerationType.IDENTITY)	// auto_increment
	@Column(nullable = false)
	private int id;
	
	@ManyToOne
	private Concert concert;
	
	@ManyToOne
	private Artist artist;
	
	@OneToOne
	private Img img;
	
	@Column(unique = true, length = 20, nullable = false)		// column을 unique 키로 설정
	private String username;
	
	@Column(nullable = false)
	private String password;	// 암호화된 password가 들어가므로 길이 짧게 지정 X
	
	@Column(length = 50, nullable = false)
	private String name;
	
	@Column(length = 50, nullable = false)
	private String email;
	
	@Column(length = 20, nullable = false)
	private String phone;
	
	@Column(length = 10, nullable = false)
	private String birth;
	
	@Column(length = 5, nullable = false)
	private String gender;
	
	@Column(length = 10, nullable = false)
	private String role;
	
	@CreationTimestamp	// default값: 현재 시간
	@Column(nullable = false)
	private LocalDateTime createdDate;
}
