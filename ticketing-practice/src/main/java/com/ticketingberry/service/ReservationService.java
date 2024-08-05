package com.ticketingberry.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ticketingberry.domain.entity.Reservation;
import com.ticketingberry.domain.entity.Seat;
import com.ticketingberry.domain.entity.User;
import com.ticketingberry.domain.repository.ReservationRepository;
import com.ticketingberry.domain.repository.SeatRepository;
import com.ticketingberry.domain.repository.UserRepository;
import com.ticketingberry.dto.ReservationDto;
import com.ticketingberry.exception.custom.DataNotFoundException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final SeatRepository seatRepository;
	private final UserRepository userRepository;
	
	// 좌석 예매
	@Transactional
	public Long createReservation(@Valid @RequestBody ReservationDto reservationDto) {
		Seat seat = seatRepository.findById(reservationDto.getSeatId())
				.orElseThrow(() -> new DataNotFoundException("좌석을 찾을 수 없습니다."));
		
		User user = userRepository.findById(reservationDto.getUserId())
				.orElseThrow(() -> new DataNotFoundException("회원을 찾을 수 없습니다."));
		
		Reservation reservation = Reservation.builder()
				.seat(seat)
				.user(user)
				.deposited(reservationDto.isDeposited())
				.build();
		
		Reservation createdReservation = reservationRepository.save(reservation);
		return createdReservation.getId();
	}
}
