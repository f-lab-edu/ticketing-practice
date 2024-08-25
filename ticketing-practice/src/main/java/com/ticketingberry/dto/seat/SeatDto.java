package com.ticketingberry.dto.seat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class SeatDto {
	@NotNull(message = "열 번호를 입력해주세요.")
	private int rowNum;
	
	@NotNull(message = "좌석 번호를 입력해주세요.")
	private int seatNum;
}
