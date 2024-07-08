package com.ticketingberry.controller;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.model.UserCreateForm;
import com.ticketingberry.model.UserUpdateForm;
import com.ticketingberry.model.entity.Article;
import com.ticketingberry.model.entity.ArticleComment;
import com.ticketingberry.model.entity.ArtistWishlist;
import com.ticketingberry.model.entity.ConcertComment;
import com.ticketingberry.model.entity.ConcertWishlist;
import com.ticketingberry.model.entity.Reservation;
import com.ticketingberry.model.entity.User;
import com.ticketingberry.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	// 회원가입 기능 수행
	@PostMapping("")
	// 작성한 내용을 validation 하는 것
	public ResponseEntity<String> join(@Valid @RequestBody UserCreateForm userCreateForm,
			  						   BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// 유효성 검사 에러가 있을 경우 400 Bad Request 반환
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request body");
		}
		
		try {
			userService.addUser(userCreateForm.getUsername(), userCreateForm.getPassword1(), userCreateForm.getNickname(), userCreateForm.getEmail(), 
								userCreateForm.getPhone(), userCreateForm.getBirth(), userCreateForm.getGender(), userCreateForm.getRole());
		} catch (DataIntegrityViolationException e) {
			// 중복 회원 등록 시 409 Conflict 반환
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
		}
		
		// 정상적으로 회원 추가됐을 경우 201 Created 반환
		return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
	}
	
	// 모든 회원 검색
	@GetMapping("")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}
	
	// 마이 페이지 => 회원 1명 검색
	@GetMapping("/{userId}")
	public ResponseEntity<User> getUser(@PathVariable("userId") Long id) {
		User user = userService.getUser(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}
	
	// 회원 정보 수정
	@PutMapping("/{userId}")
	public ResponseEntity<User> updateUser(@PathVariable("userId") Long id,
										   @Valid @RequestBody UserUpdateForm userUpdateForm,
										   BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
	        // 유효성 검사 에러가 있을 경우 400 Bad Request 반환
	        return ResponseEntity.badRequest().build();
	    }
		
		// 사용자가 존재하는지 확인
		User user = userService.getUser(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		// 사용자 정보 업데이트
		user.setNickname(userUpdateForm.getNickname());
		user.setEmail(userUpdateForm.getEmail());
		user.setPhone(userUpdateForm.getPhone());
		
		// 업데이트된 사용자 저장
		User updatedUser = userService.updateUser(user);
		
		return ResponseEntity.ok(updatedUser);
	}

	// 회원 탈퇴
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable("userId") Long id) {
		// 사용자가 존재하는지 확인
		User user = userService.getUser(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		// 사용자 삭제
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();	// 204 No Content 반환
	}
	
	// 회원 1명이 작성한 게시글 목록
	@GetMapping("/{userId}/articles")
	public ResponseEntity<List<Article>> getArticles(@PathVariable("userId") Long id) {
		User user = userService.getUser(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		List<Article> articles = userService.getArticles(user);
		return ResponseEntity.ok(articles);
	}
	
	// 회원 1명이 작성한 게시글 댓글 목록
	@GetMapping("/{userId}/article-comments")
	public ResponseEntity<List<ArticleComment>> getArticleComments(@PathVariable("userId") Long id) {
		User user = userService.getUser(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		List<ArticleComment> articleComments = userService.getArticleComments(user);
		return ResponseEntity.ok(articleComments);
	}
	
	// 회원 1명이 작성한 콘서트 댓글 목록
	@GetMapping("/{userId}/concert-comments")
	public ResponseEntity<List<ConcertComment>> getConcertComments(@PathVariable("userId") Long id) {
		User user = userService.getUser(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		List<ConcertComment> concertComments = userService.getConcertComments(user);
		return ResponseEntity.ok(concertComments);
	}
	
	// 회원 1명의 콘서트 찜 목록
	@GetMapping("{userId}/concert-wishlists")
	public ResponseEntity<List<ConcertWishlist>> getConcertWishlists(@PathVariable("userId") Long id) {
		User user = userService.getUser(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		List<ConcertWishlist> concertWishlists = userService.getConcertWishlists(user);
		return ResponseEntity.ok(concertWishlists);
	}
	
	// 회원 1명의 아티스트 찜 목록
	@GetMapping("{userId}/artist-wishlists")
	public ResponseEntity<List<ArtistWishlist>> getArtistWishlists(@PathVariable("userId") Long id) {
		User user = userService.getUser(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		List<ArtistWishlist> artistWishlists = userService.getArtistWishlists(user);
		return ResponseEntity.ok(artistWishlists);
	}
	
	// 회원 1명이 예매한 공연 목록
	@GetMapping("/{userId}/reservations")
	public ResponseEntity<List<Reservation>> getReservations(@PathVariable("userId") Long id) {
		User user = userService.getUser(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		List<Reservation> reservations = userService.getReservations(user);
		return ResponseEntity.ok(reservations);
	}
	
	// 회원 1명이 예매한 공연 1개 취소
	@DeleteMapping("/{userId}/reservations/{reservationId}")
	public ResponseEntity<?> deleteReservation(@PathVariable("userId") Long userId, 
											   @PathVariable("reservationId") Long reservationId) {
		// 사용자가 존재하는지 확인
		User user = userService.getUser(userId);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		// 예매 취소
		userService.deleteReservation(reservationId);
		return ResponseEntity.noContent().build();	// 204 No Content 반환
	}
}




