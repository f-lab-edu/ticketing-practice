package com.ticketingberry.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.ticketingberry.domain.entity.User;
import com.ticketingberry.dto.UserDto;
import com.ticketingberry.dto.UpdateUserDto;
import com.ticketingberry.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	
	// 회원 가입
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public UserDto addUser(@Valid @RequestBody UserDto userDto) {
		// 유효성 검사를 통과한 경우 사용자 생성
		User user = userService.create(userDto);
		// 정상적으로 회원 생성됐을 경우 201(생성됨)과 생성된 엔터티를 DTO로 변환하여 응답 본문으로 반환
		return UserDto.of(user);
	}
	
	// 전체 회원 목록 조회
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<UserDto> getAllUsers() {
		List<User> userList = userService.findAll();
		// 정상적으로 수행됐을 경우 200(성공)과 조회된 엔터티를 DTO로 변환하여 응답 본문으로 반환
		return userList.stream()
				.map(user -> UserDto.of(user))
				.collect(Collectors.toList());
	}
	
	// 회원 1명 조회
	@GetMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public UserDto getUser(@PathVariable("userId") Long userId) {
		User user = userService.findById(userId);
		// 정상적으로 수행됐을 경우 200(성공)과 조회된 엔터티를 DTO로 변환하여 응답 본문으로 반환
		return UserDto.of(user);
	}
	
	// 회원 정보 수정
	@PutMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public UserDto modifyUser(@PathVariable("userId") Long userId,
							 @Valid @RequestBody UpdateUserDto userUpdateDto) {
		User user = userService.update(userId, userUpdateDto);
		// 정상적으로 회원 수정됐을 경우 200(성공)과 수정된 엔터티를 DTO로 변환하여 응답 본문으로 반환
		return UserDto.of(user);
	}

	// 회원 탈퇴
	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public UserDto removeUser(@PathVariable("userId") Long userId) {
		User user = userService.delete(userId);
		// 정상적으로 수행됐을 경우 200(성공)과 삭제된 엔터티를 DTO로 변환하여 응답 본문으로 반환
		return UserDto.of(user);
	}
	
//	// 회원 1명이 작성한 게시글 목록 조회
//	@GetMapping("/{userId}/articles")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public List<Article> getArticlesByUserId(@PathVariable("userId") Long userId) {
//		return userService.findArticlesByUserId(userId);
//	}
//	
//	// 회원 1명이 작성한 게시글 댓글 목록 조회
//	@GetMapping("/{userId}/article-comments")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public List<ArticleComment> getArticleCommentsByUserId(
//			@PathVariable("userId") Long userId) {		
//		return userService.findArticleCommentsByUserId(userId);
//	}
//	
//	// 회원 1명이 작성한 콘서트 댓글 목록 조회
//	@GetMapping("/{userId}/concert-comments")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public List<ConcertComment> getConcertCommentsByUserId(
//			@PathVariable("userId") Long userId) {		
//		return userService.findConcertCommentsByUserId(userId);
//	}
//	
//	// 회원 1명의 콘서트 찜 목록 조회
//	@GetMapping("{userId}/concert-wishlists")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public List<ConcertWishlist> getConcertWishlistsByUserId(
//			@PathVariable("userId") Long userId) {
//		return userService.findConcertWishlistsByUserId(userId);
//	}
//	
//	// 회원 1명의 아티스트 찜 목록 조회
//	@GetMapping("{userId}/artist-wishlists")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public List<ArtistWishlist> getArtistWishlistsByUserId(
//			@PathVariable("userId") Long userId) {
//		return userService.findArtistWishlistsByUserId(userId);
//	}
//	
//	// 회원 1명이 예매한 공연 목록 조회
//	@GetMapping("/{userId}/reservations")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public List<Reservation> getReservationsByUserId(
//			@PathVariable("userId") Long userId) {
//		return userService.findReservationsByUserId(userId);
//	}
}

