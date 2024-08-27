package com.ticketingberry.dto.ticket;

import com.ticketingberry.domain.ticket.Ticket;
import com.ticketingberry.dto.seat.SeatResponse;
import com.ticketingberry.dto.user.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse extends TicketDto {
	private SeatResponse seatResponse;
	
	private UserResponse userResponse;
	
	public static TicketResponse of(Ticket ticket) {
		return TicketResponse.builder()
				.seatResponse(SeatResponse.of(ticket.getSeat()))
				.userResponse(UserResponse.of(ticket.getUser()))
				.deposited(ticket.isDeposited())
				.build();
	}
}
