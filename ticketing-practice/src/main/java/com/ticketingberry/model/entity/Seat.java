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
public class Seat {	// 좌석 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_id", nullable = false)
	private long id;	//  좌석 고유 id (1부터 자동 증가)
	
	@ManyToOne(fetch = FetchType.LAZY)
	private District district;		// 좌석이 속한 구역
	
	@Column(nullable = false)
	private int rowNum;				// 열 번호
	
	@Column(nullable = false)
	private int seatNum;			// 좌석 번호		
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;	// 좌석 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 좌석 객체 수정 시간
}
