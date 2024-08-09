package com.ticketingberry.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ticketingberry.domain.entity.District;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistrictDto {
	@NotNull(message = "구역에 속하는 좌석들을 추가해주세요.")
	private List<@Valid SeatDto> seatDtos;	// In
	
	@NotNull(message = "구역 이름을 입력해주세요.")
	@Size(min = 1, max = 5, message = "구역 이름을 1 ~ 5자 사이로 입력해주세요.")
	private String districtName;
	
	private Long concertId;			// Out
	
	private List<Long> seatIds;		// Out
	
	public static DistrictDto of(District district) {
		return DistrictDto.builder()
				.districtName(district.getDistrictName())
				.concertId(district.getConcert().getId())
				.seatIds(district.getSeats().stream()
						.map(seat -> seat.getId())
						.collect(Collectors.toList()))
				.build();
	}
}
