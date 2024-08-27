package com.ticketingberry.domain.board;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Board {	// 게시판 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private long id;	// 게시판 고유 id (1부터 자동 증가)
	
	@NotNull
	@Column(length = 50)
	private String name;	// 게시판 이름
	
	@CreationTimestamp
	private LocalDateTime createdAt;	// 게시판 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 게시판 객체 수정 시간
}
