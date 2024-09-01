package com.ticketingberry.dto.district;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ticketingberry.domain.district.District;
import com.ticketingberry.dto.concert.ConcertResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DistrictResponse extends DistrictDto {
	private ConcertResponse concertResponse;
	
	@Builder.Default
	private List<Long> seatIds = new ArrayList<>();
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime updatedAt;
	
	public static DistrictResponse of(District district) {
		return DistrictResponse.builder()
				.districtName(district.getDistrictName())
				.concertResponse(ConcertResponse.of(district.getConcert()))
				.seatIds(district.getSeats().stream()
						.map(seat -> seat.getId())
						.collect(Collectors.toList()))
				.createdAt(district.getCreatedAt())
				.updatedAt(district.getUpdatedAt())
				.build();
	}
}
