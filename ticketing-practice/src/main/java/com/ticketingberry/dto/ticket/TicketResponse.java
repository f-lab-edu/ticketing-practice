package com.ticketingberry.dto.reservation;

import com.ticketingberry.domain.reservation.Reservation;
import com.ticketingberry.dto.seat.OutSeatDto;
import com.ticketingberry.dto.user.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OutReservationDto extends ReservationDto {
	private OutSeatDto seatDto;
	
	private UserDto userDto;
	
	public static OutReservationDto of(Reservation reservation) {
		return OutReservationDto.builder()
				.seatDto(OutSeatDto.of(reservation.getSeat()))
				.userDto(UserDto.of(reservation.getUser()))
				.deposited(reservation.isDeposited())
				.build();
	}
}
