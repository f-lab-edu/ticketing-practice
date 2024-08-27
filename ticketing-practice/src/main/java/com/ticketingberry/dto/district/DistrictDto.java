package com.ticketingberry.dto.district;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class DistrictDto {
	@NotNull(message = "구역 이름을 입력해주세요.")
	@Size(min = 1, max = 5, message = "구역 이름을 1 ~ 5자 사이로 입력해주세요.")
	private String districtName;
}
