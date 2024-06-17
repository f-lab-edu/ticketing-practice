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
public class Place {	// 장소 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "place_id", nullable = false)
	private long id;	// 장소 고유 id (1부터 자동 증가)
	
	@Column(length = 50, nullable = false)
	private String name;	// 장소 이름
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;	// 장소 객체 생성 시간
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;	// 장소 객체 수정 시간
}
