package com.ticketingberry.domain.ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.domain.user.User;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	List<Ticket> findAll(Sort sort);
	Optional<Ticket> findBySeat(Seat seat);		// 한 개의 좌석 정보에 해당하는 티켓 정보 한 개 찾기  
	List<Ticket> findByUser(User user, Sort sort);	// 한 명의 회원이 예매한 티켓 리스트 찾기
}
