package com.ticketingberry.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ticketingberry.domain.reservation.Reservation;
import com.ticketingberry.domain.reservation.ReservationRepository;
import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.domain.seat.SeatRepository;
import com.ticketingberry.domain.user.User;
import com.ticketingberry.domain.user.UserRepository;
import com.ticketingberry.dto.reservation.InReservationDto;
import com.ticketingberry.exception.custom.DataNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final SeatRepository seatRepository;
	private final UserRepository userRepository;
	private final Sort sort = Sort.by(Sort.Order.desc("createdAt"));
	
	// 좌석 예매
	@Transactional
	public Reservation create(InReservationDto reservationDto) {
		Seat seat = findSeat(reservationDto.getSeatId());
		User user = findUser(reservationDto.getUserId());
		Reservation reservation = Reservation.of(reservationDto, seat, user);
		return reservationRepository.save(reservation);
	}
	
	// 전체 예매 목록 조회
	@Transactional
	public List<Reservation> findAll() {
		return reservationRepository.findAll(sort);
	}
	
	// 1명의 회원에 해당하는 예매 목록 조회
	@Transactional
	public List<Reservation> findListByUserId(Long userId) {
		User user = findUser(userId);
		return reservationRepository.findByUser(user, sort);
	}
	
	// 예매 1개 조회
	@Transactional
	public Reservation findById(Long reservationId) {
		return reservationRepository.findById(reservationId)
				.orElseThrow(() -> new DataNotFoundException("예매를 찾을 수 없습니다."));
	}
	
	// 예매 수정 (입금 완료)
	public Reservation update(Long reservationId, InReservationDto reservationDto) {
		Reservation reservation = findById(reservationId);
		reservation.update(reservationDto.isDeposited());
		return reservationRepository.save(reservation);
	}
	
	// 예매 취소
	public Reservation delete(Long reservationId) {
		Reservation reservation = findById(reservationId);
		reservationRepository.delete(reservation);
		return reservation;
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
