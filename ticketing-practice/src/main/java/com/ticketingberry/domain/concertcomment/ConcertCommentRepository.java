package com.ticketingberry.domain.concertcomment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.domain.user.User;

public interface ConcertCommentRepository extends JpaRepository<ConcertComment, Long> {
	List<ConcertComment> findByConcert(Concert concert);	// 한 개의 공연에 달린 댓글 리스트 찾기
	List<ConcertComment> findByUser(User user);			// 한 명의 회원이 작성한 공연의 댓글 리스트 찾기
}
