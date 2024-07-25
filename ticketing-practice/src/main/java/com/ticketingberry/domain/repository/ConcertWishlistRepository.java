package com.ticketingberry.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.entity.Concert;
import com.ticketingberry.domain.entity.ConcertWishlist;
import com.ticketingberry.domain.entity.User;
import com.ticketingberry.domain.repository.common.UserRelatedRepository;

public interface ConcertWishlistRepository extends JpaRepository<ConcertWishlist, Long>,
												   UserRelatedRepository<ConcertWishlist> {
	List<ConcertWishlist> findByUser(User user);			// 한 명의 회원이 소유한 공연 찜 리스트 찾기
	List<ConcertWishlist> findByConcert(Concert concert);	// 한 개의 공연을 찜해놓은 공연 찜 리스트 찾기
}
