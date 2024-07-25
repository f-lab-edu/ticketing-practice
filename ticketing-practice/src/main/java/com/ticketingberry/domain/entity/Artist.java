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
public class Artist {	// 아티스트 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "artist_id", nullable = false)
	private long id;	// 아티스트 고유 id (1부터 자동 증가)
	
	@OneToOne(fetch = FetchType.LAZY)
	private Image image;	// 아티스트 대표 이미지

	@Column(length = 50, nullable = false)
	private String name;	// 아티스트 이름
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;	// 아티스트 객체 생성 시간	
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 아티스트 객체 수정 시간
}
