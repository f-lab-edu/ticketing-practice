package com.ticketingberry.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticketingberry.domain.entity.Reservation;
import com.ticketingberry.domain.entity.Seat;
import com.ticketingberry.domain.entity.User;
import com.ticketingberry.domain.repository.ReservationRepository;
import com.ticketingberry.domain.repository.SeatRepository;
import com.ticketingberry.domain.repository.UserRepository;
import com.ticketingberry.dto.ReservationDto;
import com.ticketingberry.exception.custom.DataNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final SeatRepository seatRepository;
	private final UserRepository userRepository;
	
	// 좌석 예매
	@Transactional
	public Reservation createReservation(ReservationDto reservationDto) {
		Seat seat = findSeat(reservationDto.getSeatId());
		User user = findUser(reservationDto.getUserId());
		Reservation reservation = Reservation.of(reservationDto, seat, user);
		return reservationRepository.save(reservation);
	}
	
	// 전체 예매 목록 조회
	@Transactional
	public List<Reservation> findAllReservations() {
		return reservationRepository.findAll();
	}
	
	// 1명의 회원에 해당하는 예매 목록 조회
	@Transactional
	public List<Reservation> findReservationsByUserId(Long userId) {
		User user = findUser(userId);
		return reservationRepository.findByUser(user);
	}
	
	// 예매 1개 조회
	@Transactional
	public Reservation findReservationByReservationId(Long reservationId) {
		return reservationRepository.findById(reservationId)
				.orElseThrow(() -> new DataNotFoundException("예매를 찾을 수 없습니다."));
	}
	
	// 예매 수정
	public void updateReservation(Long reservationId, ReservationDto reservationDto) {
		Reservation reservation = findReservationByReservationId(reservationId);
		reservation.update(reservationDto.isDeposited());
		reservationRepository.save(reservation);
	}
	
	// 예매 삭제
	public void deleteReservation(Long reservationId) {
		Reservation reservation = findReservationByReservationId(reservationId);
		reservationRepository.delete(reservation);
	}
	
	// 1개의 좌석에 해당하는 1개의 예매 조회
	@Transactional
	public Reservation findReservationBySeatId(Long seatId) {
		Seat seat = findSeat(seatId);
		return reservationRepository.findBySeat(seat)
				.orElseThrow(() -> new DataNotFoundException("해당 좌석에 해당하는 예매를 찾을 수 없습니다."));
	}
	
	@Transactional
	private Seat findSeat(Long seatId) {
		return seatRepository.findById(seatId)
				.orElseThrow(() -> new DataNotFoundException("좌석을 찾을 수 없습니다."));
	}
	
	@Transactional
	private User findUser(Long userId) {
		return userRepository.findById(userId)
		.orElseThrow(() -> new DataNotFoundException("회원을 찾을 수 없습니다."));
	}
}
