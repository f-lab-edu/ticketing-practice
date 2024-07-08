package com.ticketingberry.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Component
public class Board {	// 게시판 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id", nullable = false)
	private long id;	// 게시판 고유 id (1부터 자동 증가)
	
	@Column(length = 50, nullable = false)
	private String name;	// 게시판 이름
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;	// 게시판 객체 생성 시간
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;	// 게시판 객체 수정 시간
}
