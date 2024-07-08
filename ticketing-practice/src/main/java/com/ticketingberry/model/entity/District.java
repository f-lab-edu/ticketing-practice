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
public class District {		// 구역 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "district_id", nullable = false)
	private long id;		// 구역 고유 id (1부터 자동 증가)
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Concert concert;		// 구역이 속한 공연
	
	@Column(length = 10, nullable = false)
	private String districtName;	// 구역 이름		
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;	// 구역 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 구역 객체 수정 시간
}
