package com.ticketingberry.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.domain.entity.District;
import com.ticketingberry.dto.DistrictDto;
import com.ticketingberry.service.DistrictService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concerts/{concertId}/districts")
public class DistrictController {
	private final DistrictService districtService;
	
	// 1개의 콘서트에 해당하는 구역 리스트 조회
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DistrictDto> getDistrictsByConcertId(@PathVariable("concertId") Long concertId) {
		List<District> districtList = districtService.findDistrictsByConcertId(concertId);
		List<DistrictDto> districtDtoList = new ArrayList<>();
		
		for (District district : districtList) {
			DistrictDto districtDto = DistrictDto.builder()
					.districtName(district.getDistrictName())
					.concertId(district.getConcert().getId())
					.seatIds(district.getSeats().stream()
							.map(seat -> seat.getId())
							.collect(Collectors.toList()))
					.build();
			
			districtDtoList.add(districtDto);
		}
		
		return districtDtoList;
	}
	
	// 1개의 구역 조회
	@GetMapping("/{districtId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistrictDto getDistrict(@PathVariable("districtId") Long districtId) {
		District district = districtService.findDistrictByDistrictId(districtId);
		
		DistrictDto districtDto = DistrictDto.builder()
				.districtName(district.getDistrictName())
				.seatIds(district.getSeats().stream()
						.map(seat -> seat.getId())
						.collect(Collectors.toList()))
				.build();
		
		return districtDto;
	}
}
