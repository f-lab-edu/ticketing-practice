package com.ticketingberry.dto.seat;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime updatedAt;
	
	public static SeatResponse of(Seat seat) {
		return SeatResponse.builder()
				.rowNum(seat.getRowNum())
				.seatNum(seat.getSeatNum())
				.districtDto(DistrictResponse.of(seat.getDistrict()))
				.createdAt(seat.getCreatedAt())
				.updatedAt(seat.getUpdatedAt())
				.build();
	}
}
