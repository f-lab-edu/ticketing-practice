package com.ticketingberry.domain.seat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.district.District;

public interface SeatRepository extends JpaRepository<Seat, Long>{
	List<Seat> findByDistrict(District district);		// 한 개의 구역에 속한 좌석 리스트 찾기
}
