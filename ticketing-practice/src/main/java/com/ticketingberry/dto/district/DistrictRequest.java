package com.ticketingberry.dto.district;

import java.util.ArrayList;
import java.util.List;

import com.ticketingberry.dto.seat.SeatDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InDistrictDto extends DistrictDto {
	@NotNull(message = "구역에 속하는 좌석들을 추가해주세요.")
	@Builder.Default
	private List<@Valid SeatDto> seatDtos = new ArrayList<>();
}
