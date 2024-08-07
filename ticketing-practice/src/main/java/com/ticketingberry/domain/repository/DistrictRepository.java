package com.ticketingberry.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.entity.Concert;
import com.ticketingberry.domain.entity.District;

public interface DistrictRepository extends JpaRepository<District, Long> {
	List<District> findByConcert(Concert concert);	// 한 개의 공연이 속해있는 구역 리스트 찾기
}
