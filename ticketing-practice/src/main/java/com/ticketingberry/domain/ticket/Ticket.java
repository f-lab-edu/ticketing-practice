package com.ticketingberry.domain.ticket;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class Ticket {	// 티켓 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_id")
	private long id;		//  티켓 고유 id (1부터 자동 증가)
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seat_id")
	private Seat seat;			// 티켓 예매를 한 좌석
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;		// 티켓 예매를 한 회원
	
	@NotNull
	private boolean deposited;	// 티켓 입금 여부
	
	@CreationTimestamp
	private LocalDateTime createdAt;	// 티켓 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 티켓 객체 수정 시간
	
	public void update(boolean deposited) {
		this.deposited = deposited;
	}
}
