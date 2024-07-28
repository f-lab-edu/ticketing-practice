package com.ticketingberry.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.domain.entity.Article;
import com.ticketingberry.domain.entity.ArticleComment;
import com.ticketingberry.domain.entity.ArtistWishlist;
import com.ticketingberry.domain.entity.ConcertComment;
import com.ticketingberry.domain.entity.ConcertWishlist;
import com.ticketingberry.domain.entity.Reservation;
import com.ticketingberry.domain.entity.User;
import com.ticketingberry.dto.UserDto;
import com.ticketingberry.dto.UpdateUserDto;
import com.ticketingberry.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;
	
	// 회원 가입
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String addUser(@Valid @RequestBody UserDto userDto) {
		// 유효성 검사를 통과한 경우 사용자 생성
		Long userId = userService.createUser(userDto);
		
		// 생성된 사용자 정보를 조회하여 문자열 형태로 생성
		String userRepresentation = "회원(id: " + userId + ") 가입에 성공했습니다.";

		// 정상적으로 회원 생성됐을 경우 201(생성됨)과 응답 본문 반환
		return userRepresentation;
	}
	
	// 전체 회원 목록 조회
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<User> getAllUsers() {
		// 정상적으로 수행됐을 경우 200(성공)과 조회된 객체 반환
		return userService.readAllUsers();
	}
	
	// 회원 1명 조회
	@GetMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public User getUser(@PathVariable("userId") Long userId) {
		return userService.findById(userId);
	}
	
	// 회원 정보 수정
	@PutMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String modifyUser(@PathVariable("userId") Long userId,
			@Valid @RequestBody UpdateUserDto userUpdateDto) {
		userService.updateUser(userId, userUpdateDto);
		return "회원 수정에 성공했습니다.";
	}

	// 회원 탈퇴
	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String removeUser(@PathVariable("userId") Long userId) {
		userService.deleteUser(userId);
		// 정상적으로 수행됐을 경우 200(성공)과 삭제된 엔터티의 id를 응답 본문으로 반환
		return "회원(id: " + userId + ") 탈퇴에 성공했습니다.";
	}
	
	// 회원 1명이 작성한 게시글 목록 조회
	@GetMapping("/{userId}/articles")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Article> getArticlesByUserId(@PathVariable("userId") Long userId) {
		return userService.findArticlesByUserId(userId);
	}
	
	// 회원 1명이 작성한 게시글 댓글 목록 조회
	@GetMapping("/{userId}/article-comments")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ArticleComment> getArticleCommentsByUserId(
			@PathVariable("userId") Long userId) {		
		return userService.findArticleCommentsByUserId(userId);
	}
	
	// 회원 1명이 작성한 콘서트 댓글 목록 조회
	@GetMapping("/{userId}/concert-comments")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConcertComment> getConcertCommentsByUserId(
			@PathVariable("userId") Long userId) {		
		return userService.findConcertCommentsByUserId(userId);
	}
	
	// 회원 1명의 콘서트 찜 목록 조회
	@GetMapping("{userId}/concert-wishlists")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConcertWishlist> getConcertWishlistsByUserId(
			@PathVariable("userId") Long userId) {
		return userService.findConcertWishlistsByUserId(userId);
	}
	
	// 회원 1명의 아티스트 찜 목록 조회
	@GetMapping("{userId}/artist-wishlists")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ArtistWishlist> getArtistWishlistsByUserId(
			@PathVariable("userId") Long userId) {
		return userService.findArtistWishlistsByUserId(userId);
	}
	
	// 회원 1명이 예매한 공연 목록 조회
	@GetMapping("/{userId}/reservations")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Reservation> getReservationsByUserId(
			@PathVariable("userId") Long userId) {
		return userService.findReservationsByUserId(userId);
	}
}

