package com.ticketingberry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Concert;
import com.ticketingberry.model.entity.ConcertComment;
import com.ticketingberry.model.entity.User;

public interface ConcertCommentRepository extends JpaRepository<ConcertComment, Long> {
	List<ConcertComment> findByConcert(Concert concert);	// 한 개의 공연에 달린 댓글 리스트 찾기
	List<ConcertComment> findByUser(User user);			// 한 명의 회원이 작성한 공연의 댓글 리스트 찾기
}
