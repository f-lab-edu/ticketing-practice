package com.ticketingberry.domain.concert;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.artist.Artist;
import com.ticketingberry.domain.place.Place;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
	List<Concert> findAll(Sort sort);
	List<Concert> findByPlace(Place place, Sort sort);			// 한 곳의 장소에서 열리는 공연 리스트 찾기
	List<Concert> findByArtist(Artist artist, Sort sort);	// 한 팀의 아티스트가 진행하는 공연 리스트 찾기
}	
