package com.ticketingberry.dto.concert;

import java.util.ArrayList;
import java.util.List;

import com.ticketingberry.dto.district.InDistrictDto;

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
public class InConcertDto extends ConcertDto {
	@NotNull(message = "공연이 열리는 장소를 선택해주세요.")
	private Long placeId;
	
	@NotNull(message = "공연을 진행하는 아티스트를 선택해주세요.")
	private Long artistId;
	
	@NotNull(message = "공연에 속하는 구역들을 추가해주세요.")
	@Builder.Default
	private List<@Valid InDistrictDto> districtDtos = new ArrayList<>();
}
