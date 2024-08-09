package com.ticketingberry.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.domain.entity.Reservation;
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
	public ReservationDto addReservation(@Valid @RequestBody ReservationDto reservationDto) {
		Reservation reservation = reservationService.createReservation(reservationDto);
		return ReservationDto.of(reservation);
	}
}
