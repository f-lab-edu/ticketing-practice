package com.ticketingberry.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.domain.reservation.Reservation;
import com.ticketingberry.dto.reservation.InReservationDto;
import com.ticketingberry.dto.reservation.OutReservationDto;
import com.ticketingberry.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationService reservationService;
	
	// 좌석 예매
	@PostMapping("/reservations")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public OutReservationDto addReservation(@Valid @RequestBody InReservationDto inReservationDto) {
		Reservation reservation = reservationService.create(inReservationDto);
		return OutReservationDto.of(reservation);
	}
	
	// 전체 예매 목록 조회
	@GetMapping("/reservations")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<OutReservationDto> getAllReservations() {
		List<Reservation> reservationList = reservationService.findAll();
		return entityListToDtoList(reservationList);
	}
	
	// 1명의 회원에 해당하는 예매 목록 조회
	@GetMapping("/users/{userId}/reservations")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<OutReservationDto> getReservationsByUserId(@PathVariable("userId") Long userId) {
		List<Reservation> reservationList = reservationService.findListByUserId(userId);
		return entityListToDtoList(reservationList);
	}
	
	// 예매 1개 조회
	@GetMapping("/reservations/{reservationId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public OutReservationDto getReservation(@PathVariable("reservationId") Long reservationId) {
		Reservation reservation = reservationService.findById(reservationId);
		return OutReservationDto.of(reservation);
	}
	
	// 예매 수정 (입금 완료)
	@PutMapping("/reservations/{reservationId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public OutReservationDto modifyReservation(@PathVariable("reservationId") Long reservationId,
											   @Valid @RequestBody InReservationDto inReservationDto) {
		Reservation reservation = reservationService.update(reservationId, inReservationDto);
		return OutReservationDto.of(reservation);
	}
	
	// 예매 취소
	@DeleteMapping("/reservations/{reservationId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public OutReservationDto removeReservation(@PathVariable("reservationId") Long reservationId) {
		Reservation reservation = reservationService.delete(reservationId);
		return OutReservationDto.of(reservation);
	}
	
	private List<OutReservationDto> entityListToDtoList(List<Reservation> reservationList) {
		return reservationList.stream()
				.map(reservation -> OutReservationDto.of(reservation))
				.collect(Collectors.toList());
	}
}
