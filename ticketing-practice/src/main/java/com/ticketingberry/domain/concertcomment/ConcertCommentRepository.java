package com.ticketingberry.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.entity.Concert;
import com.ticketingberry.domain.entity.ConcertComment;
import com.ticketingberry.domain.entity.User;
import com.ticketingberry.domain.repository.common.UserRelatedRepository;

public interface ConcertCommentRepository extends JpaRepository<ConcertComment, Long>,
												  UserRelatedRepository<ConcertComment> {
	List<ConcertComment> findByConcert(Concert concert);	// 한 개의 공연에 달린 댓글 리스트 찾기
	List<ConcertComment> findByUser(User user);			// 한 명의 회원이 작성한 공연의 댓글 리스트 찾기
}
