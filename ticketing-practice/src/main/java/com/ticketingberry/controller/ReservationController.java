package com.ticketingberry.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.domain.entity.Reservation;
import com.ticketingberry.service.ReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
	private final ReservationService reservationService;
	
	// 예매 1개 조회
	@GetMapping("/{reservationId}")
	public ResponseEntity<Reservation> getReservation(@PathVariable("reservationId") Long reservationId) {
		Reservation reservation = reservationService.readReservation(reservationId);
		return ResponseEntity.ok(reservation);
	}
	
	// 예매 취소
	@DeleteMapping("/{reservationId}")
	public ResponseEntity<String> removeReservation(@PathVariable("reservationId") Long reservationId) {
		reservationService.deleteReservation(reservationId);
		// 정상적으로 수행됐을 경우 200(성공)과 삭제된 엔터티의 id를 응답 본문으로 반환
		return ResponseEntity.status(HttpStatus.OK).body("예매(id: " + reservationId + ") 취소에 성공했습니다.");
	}
}
