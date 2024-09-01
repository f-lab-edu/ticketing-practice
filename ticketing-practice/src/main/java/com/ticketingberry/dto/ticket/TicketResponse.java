package com.ticketingberry.dto.ticket;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime updatedAt;
	
	public static TicketResponse of(Ticket ticket) {
		return TicketResponse.builder()
				.seatResponse(SeatResponse.of(ticket.getSeat()))
				.userResponse(UserResponse.of(ticket.getUser()))
				.deposited(ticket.isDeposited())
				.createdAt(ticket.getCreatedAt())
				.updatedAt(ticket.getUpdatedAt())
				.build();
	}
}
