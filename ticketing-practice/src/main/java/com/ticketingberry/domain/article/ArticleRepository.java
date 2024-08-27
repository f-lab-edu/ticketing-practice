package com.ticketingberry.domain.article;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.board.Board;
import com.ticketingberry.domain.user.User;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	List<Article> findByBoard(Board board);		// 한 개의 게시판에 속한 게시글 리스트 찾기
	List<Article> findByUser(User user);		// 한 명의 회원이 작성한 게시글 리스트 찾기
	// List<Article> findByKeywordContainingIgnoreCase(String keyword);	// 한 개의 제목+내용 키워드로 게시글 리스트 찾기
}
