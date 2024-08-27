package com.ticketingberry.dto.ticket;

import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.domain.ticket.Ticket;
import com.ticketingberry.domain.user.User;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest extends TicketDto {
	@NotNull(message = "티켓 예매를 한 좌석을 선택해주세요.")
	private Long seatId;
	
	@NotNull(message = "티켓 예매를 한 회원을 선택해주세요.")
	private Long userId;
	
	public static Ticket newTicket(TicketRequest ticketRequest, Seat seat, User user) {
		return Ticket.builder()
				.seat(seat)
				.user(user)
				.deposited(ticketRequest.isDeposited())
				.build();
	}
}
