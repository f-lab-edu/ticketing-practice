package com.ticketingberry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Board;
import com.ticketingberry.model.entity.User;

public interface BoardRepository extends JpaRepository<Board, Integer> {
	List<Board> findByUser(User user);		// 한 명의 회원으로 게시판 리스트 찾기
	// List<Board> findByKeywordContainingIgnoreCase(String keyword);	// 한 개의 제목+내용 키워드로 게시판 리스트 찾기
}
