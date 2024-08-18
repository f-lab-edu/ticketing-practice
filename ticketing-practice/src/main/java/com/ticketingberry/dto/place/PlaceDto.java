package com.ticketingberry.dto.place;

import com.ticketingberry.domain.place.Place;

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
public class PlaceDto {
	@NotNull(message = "공연 장소 이름을 입력해주세요.")
	@Size(min = 1, max = 50, message = "공연 장소 이름을 1 ~ 16자 사이로 입력해주세요.")
	private String name;
	
	public static PlaceDto of(Place place) {
		return PlaceDto.builder()
				.name(place.getName())
				.build();
	}
}
