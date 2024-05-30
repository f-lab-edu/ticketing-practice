package com.ticketingberry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Board;
import com.ticketingberry.model.entity.Concert;
import com.ticketingberry.model.entity.Reply;
import com.ticketingberry.model.entity.User;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
	List<Reply> findByConcert(Concert concert);		// 한 개의 콘서트로 댓글 리스트 가져오기
	List<Reply> findByBoard(Board board);			// 한 개의 게시판으로 댓글 리스트 가져오기
	List<Reply> findByUser(User user);				// 한 명의 회원으로 댓글 리스트 가져오기
}
