package com.ticketingberry.domain.reservation;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.domain.user.User;
import com.ticketingberry.dto.reservation.InReservationDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Reservation {	// 예매 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id")
	private long id;		//  예매 고유 id (1부터 자동 증가)
	
	@OneToOne(fetch = FetchType.LAZY)
	private Seat seat;			// 예매를 한 좌석
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;		// 예매를 한 회원
	
	@NotNull
	private boolean deposited;	// 예매 입금 여부
	
	@CreationTimestamp
	private LocalDateTime createdAt;	// 예매 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 예매 객체 수정 시간
	
	public static Reservation of(InReservationDto reservationDto, Seat seat, User user) {
		return Reservation.builder()
				.seat(seat)
				.user(user)
				.deposited(reservationDto.isDeposited())
				.build();
	}
	
	public void update(boolean deposited) {
		this.deposited = deposited;
	}
}
