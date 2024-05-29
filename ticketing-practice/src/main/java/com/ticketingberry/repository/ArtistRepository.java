package com.ticketingberry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Artist;
import com.ticketingberry.model.entity.Concert;
import com.ticketingberry.model.entity.User;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
	List<Artist> findByConcert(Concert concert);	// 한 개의 콘서트로 아티스트 리스트 찾기
	List<Artist> findByUser(User user);				// 한 명의 회원으로 아티스트 리스트 찾기
}
