package com.ticketingberry.domain.entity;

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
public class ArticleComment {	// 게시글의 댓글 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_comment_id", nullable = false)
	private long id;		// 게시글의 댓글 고유 id (1부터 자동 증가)
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Article article;	// 댓글을 단 게시글
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;		// 게시글의 댓글을 작성한 회원
	
	@Column(length = 5000, nullable = false)
	private String content;		// 게시글의 댓글 내용
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;	// 게시글의 댓글 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 게시글의 댓글 객체 수정 시간
}
