package com.ticketingberry.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.domain.entity.Seat;
import com.ticketingberry.dto.SeatDto;
import com.ticketingberry.service.SeatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SeatController {
	private final SeatService seatService;
	
	// 1개의 구역에 해당하는 좌석 리스트 조회
	@GetMapping("/districts/{districtId}/seats")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SeatDto> getSeatsByDistrictId(@PathVariable("districtId") Long districtId) {
		List<Seat> seatList = seatService.findListByDistrictId(districtId);
		return seatList.stream()
				.map(seat -> SeatDto.of(seat))
				.collect(Collectors.toList());
	}
	
	// 1개의 좌석 조회
	@GetMapping("/seats/{seatId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SeatDto getSeat(@PathVariable("seatId") Long seatId) {
		Seat seat = seatService.findById(seatId);
		return SeatDto.of(seat);
	}
}
