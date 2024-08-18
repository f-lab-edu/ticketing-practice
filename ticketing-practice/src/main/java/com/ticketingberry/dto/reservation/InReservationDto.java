package com.ticketingberry.dto.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InReservationDto extends ReservationDto {
	@NotNull(message = "예매를 한 좌석을 선택해주세요.")
	private Long seatId;
	
	@NotNull(message = "예매를 한 회원을 선택해주세요.")
	private Long userId;
}
