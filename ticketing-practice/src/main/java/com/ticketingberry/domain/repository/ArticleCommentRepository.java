package com.ticketingberry.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.entity.Article;
import com.ticketingberry.domain.entity.ArticleComment;
import com.ticketingberry.domain.entity.User;
import com.ticketingberry.domain.repository.common.UserRelatedRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long>, 
												  UserRelatedRepository<ArticleComment> {
	List<ArticleComment> findByArticle(Article article);	// 한 개의 게시글에 달린 댓글 리스트 찾기
	List<ArticleComment> findByUser(User user);			// 한 명의 회원이 작성한 게시글의 댓글 리스트 찾기
}
