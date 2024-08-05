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
public class ReservationDto {
	@NotNull(message = "예매를 한 좌석을 선택해주세요.")
	private Long seatId;
	
	@NotNull(message = "예매를 한 회원을 선택해주세요.")
	private Long userId;
	
	private boolean deposited;
}
