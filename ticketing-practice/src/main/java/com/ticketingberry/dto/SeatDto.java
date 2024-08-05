package com.ticketingberry.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto {
	@NotNull(message = "열 번호를 입력해주세요.")
	private int rowNum;
	
	@NotNull(message = "좌석 번호를 입력해주세요.")
	private int seatNum;
	
	private Long districtId;	// Out
}
