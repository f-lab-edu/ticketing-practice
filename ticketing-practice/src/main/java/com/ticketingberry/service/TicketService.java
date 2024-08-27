package com.ticketingberry.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.domain.seat.SeatRepository;
import com.ticketingberry.domain.ticket.Ticket;
import com.ticketingberry.domain.ticket.TicketRepository;
import com.ticketingberry.domain.user.User;
import com.ticketingberry.domain.user.UserRepository;
import com.ticketingberry.dto.ticket.TicketRequest;
import com.ticketingberry.exception.custom.DataNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {
	private final TicketRepository ticketRepository;
	private final SeatRepository seatRepository;
	private final UserRepository userRepository;
	private final Sort sort = Sort.by(Sort.Order.desc("createdAt"));
	
	// 티켓 예매
	@Transactional
	public Ticket create(TicketRequest ticketRequest) {
		Seat seat = findSeat(ticketRequest.getSeatId());
		User user = findUser(ticketRequest.getUserId());
		Ticket reservation = TicketRequest.newTicket(ticketRequest, seat, user);
		return ticketRepository.save(reservation);
	}
	
	// 전체 티켓 목록 조회
	@Transactional
	public List<Ticket> findAll() {
		return ticketRepository.findAll(sort);
	}
	
	// 1명의 회원에 해당하는 티켓 목록 조회
	@Transactional
	public List<Ticket> findListByUserId(Long userId) {
		User user = findUser(userId);
		return ticketRepository.findByUser(user, sort);
	}
	
	// 티켓 1개 조회
	@Transactional
	public Ticket findById(Long ticketId) {
		return ticketRepository.findById(ticketId)
				.orElseThrow(() -> new DataNotFoundException("티켓을 찾을 수 없습니다."));
	}
	
	// 티켓 수정 (입금 완료)
	public Ticket update(Long ticketId, TicketRequest ticketRequest) {
		Ticket ticket = findById(ticketId);
		ticket.update(ticketRequest.isDeposited());
		return ticketRepository.save(ticket);
	}
	
	// 티켓 예매 취소
	public Ticket delete(Long ticketId) {
		Ticket ticket = findById(ticketId);
		ticketRepository.delete(ticket);
		return ticket;
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
