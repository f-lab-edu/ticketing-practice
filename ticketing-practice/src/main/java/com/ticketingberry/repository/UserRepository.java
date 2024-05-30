package com.ticketingberry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Artist;
import com.ticketingberry.model.entity.Concert;
import com.ticketingberry.model.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);		// 한 개의 아이디로 회원 찾기
	List<User> findByConcert(Concert concert);			// 한 개의 콘서트로 회원 리스트 찾기
	List<User> findByArtist(Artist Artist);				// 한 팀의 아티스트로 회원 리스트 찾기
}
