package com.ticketingberry.domain.concertcomment;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class ConcertComment {	// 콘서트(공연)의 댓글 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@Column(name = "concert_comment_id")
	private long id;		// 공연의 댓글 고유 id (1부터 자동 증가)
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Concert concert;	// 댓글을 단 공연
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;		// 공연의 댓글을 작성한 회원
	
	@NotNull
	@Column(length = 5000)
	private String content;		// 공연의 댓글 내용
	
	@CreationTimestamp
	private LocalDateTime createdAt;	// 공연의 댓글 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 공연의 댓글 객체 수정 시간
}
