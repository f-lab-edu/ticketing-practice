package com.ticketingberry.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticketingberry.domain.entity.Reservation;
import com.ticketingberry.domain.repository.ReservationRepository;
import com.ticketingberry.exception.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationRepository reservationRepository;
	
	// 예매 1개 조회
	public Reservation readReservation(Long reservationId) {
		return reservationRepository.findById(reservationId)
				.orElseThrow(() -> new DataNotFoundException("예매를 찾을 수 없습니다."));
	}
	
	// 예매 취소
	@Transactional
	public void deleteReservation(Long reservationId) {
		Reservation reservation = readReservation(reservationId);
		reservationRepository.delete(reservation);
	}
}
