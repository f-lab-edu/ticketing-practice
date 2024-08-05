package com.ticketingberry.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.domain.entity.Seat;
import com.ticketingberry.dto.SeatDto;
import com.ticketingberry.service.SeatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concerts/{concertId}/districts/{districtId}/seats")
public class SeatController {
	private final SeatService seatService;
	
	// 1개의 구역에 해당하는 좌석 리스트 조회
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SeatDto> getSeatsByDistrictId(@PathVariable("districtId") Long districtId) {
		List<Seat> seatList = seatService.findSeatsByDistrictId(districtId);
		List<SeatDto> seatDtoList = new ArrayList<>();
		
		for (Seat seat : seatList) {
			SeatDto seatDto = SeatDto.builder()
					.rowNum(seat.getRowNum())
					.seatNum(seat.getSeatNum())
					.districtId(seat.getDistrict().getId())
					.build();
			
			seatDtoList.add(seatDto);
		}
		
		return seatDtoList;
	}
	
	// 1개의 좌석 조회
	@GetMapping("/{seatId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SeatDto getSeat(@PathVariable("seatId") Long seatId) {
		Seat seat = seatService.findSeatBySeatId(seatId);
		
		SeatDto seatDto = SeatDto.builder()
				.rowNum(seat.getRowNum())
				.seatNum(seat.getSeatNum())
				.districtId(seat.getDistrict().getId())
				.build();
		
		return seatDto;
	}
}
