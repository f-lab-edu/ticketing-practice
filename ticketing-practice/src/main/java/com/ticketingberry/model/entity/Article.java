package com.ticketingberry.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Article {	// 게시글 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_id", nullable = false)
	private long id;		// 게시글 고유 id (1부터 자동 증가)
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Board board;		// 게시글이 속한 게시판
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;		// 게시글을 작성한 회원
	
	@OneToOne(fetch = FetchType.LAZY)
	private Image image;		// 게시글에 첨부된 이미지
	
	@Column(length = 200, nullable = false)
	private String title;		// 게시글 제목
	
	@Column(length = 5000, nullable = false)
	private String content;		// 게시글 내용
	
	@Column(nullable = false)
	private long hits;		// 게시글 조회수 (0부터 증가)
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;	// 게시글 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 게시글 객체 수정 시간
}
