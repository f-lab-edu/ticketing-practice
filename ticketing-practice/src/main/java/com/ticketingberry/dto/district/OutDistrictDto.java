package com.ticketingberry.dto.district;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ticketingberry.domain.district.District;
import com.ticketingberry.dto.concert.OutConcertDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OutDistrictDto extends DistrictDto {
	private OutConcertDto concertDto;
	
	@Builder.Default
	private List<Long> seatIds = new ArrayList<>();
	
	public static OutDistrictDto of(District district) {
		return OutDistrictDto.builder()
				.districtName(district.getDistrictName())
				.concertDto(OutConcertDto.of(district.getConcert()))
				.seatIds(district.getSeats().stream()
						.map(seat -> seat.getId())
						.collect(Collectors.toList()))
				.build();
	}
}
