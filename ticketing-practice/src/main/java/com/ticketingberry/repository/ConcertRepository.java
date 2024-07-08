package com.ticketingberry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Artist;
import com.ticketingberry.model.entity.Concert;
import com.ticketingberry.model.entity.Place;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
	List<Concert> findByPlace(Place place);			// 한 곳의 장소에서 열리는 공연 리스트 찾기
	List<Concert> findByPerformer(Artist performer);	// 한 팀의 아티스트가 진행하는 공연 리스트 찾기
}	
