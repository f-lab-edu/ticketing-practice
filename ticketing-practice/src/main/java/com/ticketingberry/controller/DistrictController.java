package com.ticketingberry.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.domain.district.District;
import com.ticketingberry.dto.district.OutDistrictDto;
import com.ticketingberry.service.DistrictService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DistrictController {
	private final DistrictService districtService;
	
	// 1개의 콘서트에 해당하는 구역 리스트 조회
	@GetMapping("/concerts/{concertId}/districts")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<OutDistrictDto> getDistrictsByConcertId(@PathVariable("concertId") Long concertId) {
		List<District> districtList = districtService.findListByConcertId(concertId);
		return districtList.stream()
				.map(district -> OutDistrictDto.of(district))
				.collect(Collectors.toList());
	}
	
	// 1개의 구역 조회
	@GetMapping("/districts/{districtId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public OutDistrictDto getDistrict(@PathVariable("districtId") Long districtId) {
		District district = districtService.findById(districtId);
		return OutDistrictDto.of(district);
	}
}
