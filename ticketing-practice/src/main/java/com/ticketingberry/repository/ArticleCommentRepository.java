package com.ticketingberry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Article;
import com.ticketingberry.model.entity.ArticleComment;
import com.ticketingberry.model.entity.User;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
	List<ArticleComment> findByArticle(Article article);	// 한 개의 게시글에 달린 댓글 리스트 찾기
	List<ArticleComment> findByUser(User user);			// 한 명의 회원이 작성한 게시글의 댓글 리스트 찾기
}
