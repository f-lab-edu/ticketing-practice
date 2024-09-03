package com.ticketingberry.domain.seat;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import com.ticketingberry.domain.district.District;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class Seat {	// 좌석 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_id")
	private long id;	//  좌석 고유 id (1부터 자동 증가)
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district_id")
	private District district;		// 좌석이 속한 구역
	
	@NotNull
	private int rowNum;				// 열 번호
	
	@NotNull
	private int seatNum;			// 좌석 번호		
	
	@CreationTimestamp
	private LocalDateTime createdAt;	// 좌석 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 좌석 객체 수정 시간
}
