package com.ticketingberry.domain.district;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.domain.seat.Seat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class District {		// 구역 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "district_id")
	private long id;		// 구역 고유 id (1부터 자동 증가)
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "concert_id")
	private Concert concert;		// 구역이 속한 공연
	
	@OneToMany(mappedBy = "district", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Seat> seats = new ArrayList<>();		// 구역에 속한 좌석들
	
	@NotNull
	@Column(length = 10)
	private String districtName;	// 구역 이름
	
	@CreationTimestamp
	private LocalDateTime createdAt;	// 구역 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 구역 객체 수정 시간
}
