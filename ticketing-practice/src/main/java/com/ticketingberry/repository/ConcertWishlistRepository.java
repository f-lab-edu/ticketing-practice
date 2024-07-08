package com.ticketingberry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Concert;
import com.ticketingberry.model.entity.ConcertWishlist;
import com.ticketingberry.model.entity.User;

public interface ConcertWishlistRepository extends JpaRepository<ConcertWishlist, Long>{
	List<ConcertWishlist> findByUser(User user);			// 한 명의 회원이 소유한 공연 찜 리스트 찾기
	List<ConcertWishlist> findByConcert(Concert concert);	// 한 개의 공연을 찜해놓은 공연 찜 리스트 찾기
}
