package com.ticketingberry.dto.district;

import java.util.ArrayList;
import java.util.List;

import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.domain.district.District;
import com.ticketingberry.dto.seat.SeatRequest;

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
public class DistrictRequest extends DistrictDto {
	@NotNull(message = "구역에 속하는 좌석들을 추가해주세요.")
	@Builder.Default
	private List<@Valid SeatRequest> seatRequests = new ArrayList<>();
	
	public static District newDistrict(DistrictRequest districtRequest, Concert concert) {
		return District.builder()
				.concert(concert)
				.districtName(districtRequest.getDistrictName())
				.build();
	}
}
