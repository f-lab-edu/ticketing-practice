package com.ticketingberry.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ticketingberry.domain.entity.Article;
import com.ticketingberry.domain.entity.ArticleComment;
import com.ticketingberry.domain.entity.ArtistWishlist;
import com.ticketingberry.domain.entity.ConcertComment;
import com.ticketingberry.domain.entity.ConcertWishlist;
import com.ticketingberry.domain.entity.Reservation;
import com.ticketingberry.domain.entity.User;
import com.ticketingberry.dto.UserDto;
import com.ticketingberry.dto.UserUpdateRequest;
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
	public ResponseEntity<String> addUser(@Valid @RequestBody UserDto userDto) {
		// 유효성 검사를 통과한 경우 사용자 생성
		Long userId = userService.createUser(userDto);
		
		// 생성된 사용자의 URI 생성
		String locationUri = ServletUriComponentsBuilder.fromCurrentRequest()
										.path("/{userId}")
										.buildAndExpand(userId)
										.toUriString();
		
		// 생성된 사용자 정보를 조회하여 문자열 형태로 생성
		String userRepresentation = "회원(id: " + userId + ") 가입에 성공했습니다.";
		
		// 정상적으로 회원 생성됐을 경우 201(생성됨), Location 헤더와 응답 본문 반환
		return ResponseEntity.created(URI.create(locationUri)).body(userRepresentation);
	}
	
	// 전체 회원 목록 조회
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.readAllUsers();
		// 정상적으로 수행됐을 경우 200(성공)과 조회된 객체 반환
		return ResponseEntity.ok(users);
	}
	
	// 회원 1명 조회
	@GetMapping("/{userId}")
	public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
		User user = userService.readUser(userId);
		return ResponseEntity.ok(user);
	}
	
	// 회원 정보 수정
	@PutMapping("/{userId}")
	public ResponseEntity<String> modifyUser(@PathVariable("userId") Long userId,
										   @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
		userService.updateUser(userId, userUpdateRequest);
		return ResponseEntity.status(HttpStatus.OK).body("회원 수정에 성공했습니다.");
	}

	// 회원 탈퇴
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> removeUser(@PathVariable("userId") Long userId) {
		userService.deleteUser(userId);
		// 정상적으로 수행됐을 경우 200(성공)과 삭제된 엔터티의 id를 응답 본문으로 반환
		return ResponseEntity.status(HttpStatus.OK).body("회원(id: " + userId + ") 탈퇴에 성공했습니다.");
	}
	
	// 회원 1명이 작성한 게시글 목록 조회
	@GetMapping("/{userId}/articles")
	public ResponseEntity<List<Article>> getArticlesByUserId(
												@PathVariable("userId") Long userId) {
		List<Article> articles = userService.readArticlesByUserId(userId);
		return ResponseEntity.ok(articles);
	}
	
	// 회원 1명이 작성한 게시글 댓글 목록 조회
	@GetMapping("/{userId}/article-comments")
	public ResponseEntity<List<ArticleComment>> getArticleCommentsByUserId(
												@PathVariable("userId") Long userId) {		
		List<ArticleComment> articleComments = userService.readArticleCommentsByUserId(userId);
		return ResponseEntity.ok(articleComments);
	}
	
	// 회원 1명이 작성한 콘서트 댓글 목록 조회
	@GetMapping("/{userId}/concert-comments")
	public ResponseEntity<List<ConcertComment>> getConcertCommentsByUserId(
												@PathVariable("userId") Long userId) {		
		List<ConcertComment> concertComments = userService.readConcertCommentsByUserId(userId);
		return ResponseEntity.ok(concertComments);
	}
	
	// 회원 1명의 콘서트 찜 목록 조회
	@GetMapping("{userId}/concert-wishlists")
	public ResponseEntity<List<ConcertWishlist>> getConcertWishlistsByUserId(
												@PathVariable("userId") Long userId) {
		List<ConcertWishlist> concertWishlists = userService.readConcertWishlistsByUserId(userId);
		return ResponseEntity.ok(concertWishlists);
	}
	
	// 회원 1명의 아티스트 찜 목록 조회
	@GetMapping("{userId}/artist-wishlists")
	public ResponseEntity<List<ArtistWishlist>> getArtistWishlistsByUserId(
												@PathVariable("userId") Long userId) {
		List<ArtistWishlist> artistWishlists = userService.readArtistWishlistsByUserId(userId);
		return ResponseEntity.ok(artistWishlists);
	}
	
	// 회원 1명이 예매한 공연 목록 조회
	@GetMapping("/{userId}/reservations")
	public ResponseEntity<List<Reservation>> getReservationsByUserId(
												@PathVariable("userId") Long userId) {
		List<Reservation> reservations = userService.readReservationsByUserId(userId);
		return ResponseEntity.ok(reservations);
	}
}
