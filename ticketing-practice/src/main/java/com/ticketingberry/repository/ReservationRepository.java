package com.ticketingberry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Reservation;
import com.ticketingberry.model.entity.Seat;
import com.ticketingberry.model.entity.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	Optional<Reservation> findBySeat(Seat seat);		// 한 개의 좌석 정보에 해당하는 예매 정보 한 개 찾기  
	List<Reservation> findByUser(User user);	// 한 명의 회원이 예매한 예매 리스트 찾기
}
