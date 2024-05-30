package com.ticketingberry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Artist;
import com.ticketingberry.model.entity.Concert;
import com.ticketingberry.model.entity.Place;
import com.ticketingberry.model.entity.User;

public interface ConcertRepository extends JpaRepository<Concert, Integer> {
	List<Concert> findByPlace(Place place);			// 한 곳의 장소로 콘서트 리스트 찾기
	List<Concert> findByArtist(Artist artist);		// 한 팀의 아티스트로 콘서트 리스트 찾기
	List<Concert> findByUser(User user);			// 한 명의 회원으로 콘서트 리스트 찾기
}	
