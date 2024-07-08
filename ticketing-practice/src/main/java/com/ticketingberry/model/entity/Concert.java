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
public class Concert {	// 콘서트(공연) 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "concert_id", nullable = false)
	private long id;		// 공연 고유 id (1부터 자동 증가)
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Place place;		// 공연이 열리는 장소
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Artist artist;	// 공연을 진행하는 아티스트
	
	@OneToOne(fetch = FetchType.LAZY)
	private Image image;		// 공연 대표 이미지
	
	@Column(length = 200, nullable = false)
	private String title;		// 공연 제목
	
	@Column(length = 5000, nullable = false)
	private String content;		// 공연 내용
	
	@Column(nullable = false)
	private long hits;		// 공연 조회수
	
	@Column(nullable = false)
	private LocalDateTime openedTicketAt;	// 공연 예매가 열리는 시간
	
	@Column(nullable = false)
	private LocalDateTime performedAt;		// 공연이 시작하는 시간	
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;	// 공연 객체 생성 시간

	@LastModifiedDate
	private LocalDateTime updatedAt;	// 공연 객체 수정 시간
}
