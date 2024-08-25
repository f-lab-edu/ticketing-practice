package com.ticketingberry.dto.seat;

import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.dto.district.OutDistrictDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OutSeatDto extends SeatDto {
	private OutDistrictDto districtDto;
	
	public static OutSeatDto of(Seat seat) {
		return OutSeatDto.builder()
				.rowNum(seat.getRowNum())
				.seatNum(seat.getSeatNum())
				.districtDto(OutDistrictDto.of(seat.getDistrict()))
				.build();
	}
}
