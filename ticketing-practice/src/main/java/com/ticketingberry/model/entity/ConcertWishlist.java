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
public class ConcertWishlist {	// 콘서트(공연) 찜 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "concert_wishlist_id", nullable = false)
	private long id;		//  공연 찜 고유 id (1부터 자동 증가)
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Concert concert;	// 찜을 해놓은 공연
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;			// 공연 찜을 소유하고 있는 회원
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;	// 공연 찜 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 공연 찜 객체 수정 시간
}
