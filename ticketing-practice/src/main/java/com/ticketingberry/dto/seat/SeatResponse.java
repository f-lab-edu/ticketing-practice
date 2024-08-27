package com.ticketingberry.dto.seat;

import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.dto.district.DistrictResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse extends SeatDto {
	private DistrictResponse districtDto;
	
	public static SeatResponse of(Seat seat) {
		return SeatResponse.builder()
				.rowNum(seat.getRowNum())
				.seatNum(seat.getSeatNum())
				.districtDto(DistrictResponse.of(seat.getDistrict()))
				.build();
	}
}
