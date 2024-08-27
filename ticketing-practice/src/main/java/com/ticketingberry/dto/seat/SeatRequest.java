package com.ticketingberry.dto.seat;

import com.ticketingberry.domain.district.District;
import com.ticketingberry.domain.seat.Seat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class SeatRequest extends SeatDto {
	public static Seat newSeat(SeatRequest seatRequest, District district) {
		return Seat.builder()
				.district(district)
				.rowNum(seatRequest.getRowNum())
				.seatNum(seatRequest.getSeatNum())
				.build();
	}
}
