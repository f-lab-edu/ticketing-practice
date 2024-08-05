package com.ticketingberry.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.dto.ReservationDto;
import com.ticketingberry.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationService reservationService;
	
	// 좌석 예매
	@PostMapping("/reservation")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String addReservation(@Valid @RequestBody ReservationDto reservationDto) {
		Long reservationId = reservationService.createReservation(reservationDto);
		return "예매(id: " + reservationId + ")에 성공했습니다.";
	}
}
