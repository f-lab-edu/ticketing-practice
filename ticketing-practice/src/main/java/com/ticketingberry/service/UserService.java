package com.ticketingberry.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticketingberry.domain.entity.Article;
import com.ticketingberry.domain.entity.ArticleComment;
import com.ticketingberry.domain.entity.ArtistWishlist;
import com.ticketingberry.domain.entity.ConcertComment;
import com.ticketingberry.domain.entity.ConcertWishlist;
import com.ticketingberry.domain.entity.Reservation;
import com.ticketingberry.domain.entity.User;
import com.ticketingberry.domain.repository.ArticleCommentRepository;
import com.ticketingberry.domain.repository.ArticleRepository;
import com.ticketingberry.domain.repository.ArtistWishlistRepository;
import com.ticketingberry.domain.repository.ConcertCommentRepository;
import com.ticketingberry.domain.repository.ConcertWishlistRepository;
import com.ticketingberry.domain.repository.ReservationRepository;
import com.ticketingberry.domain.repository.UserRepository;
import com.ticketingberry.dto.UserDto;
import com.ticketingberry.dto.UserUpdateRequest;
import com.ticketingberry.exception.DataNotFoundException;
import com.ticketingberry.exception.DuplicatedExcpetion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor	// 'final'이 붙은 필드 중에 초기화 되지 않은 모든 필드를 인자로 설정한 생성자를 만들어 줌
public class UserService {
	private final UserRepository userRepository;
	private final ArticleRepository articleRepository;
	private final ArticleCommentRepository articleCommentRepository;
	private final ConcertCommentRepository concertCommentRepository;
	private final ConcertWishlistRepository concertWishlistRepository;
	private final ArtistWishlistRepository artistWishlistRepository;
	private final ReservationRepository reservationRepository;
	private final PasswordEncoder passwordEncoder;
	
	// 회원 생성
	@Transactional
	public Long createUser(UserDto userDto) {
		// 비밀번호와 비밀번호 확인이 다를 경우 400(잘못된 요청) 던지기
		if (!userDto.getPassword1().equals(userDto.getPassword2())) {
			throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 다릅니다.");
		}
		
		// username으로 회원 조회가 된 경우 중복 객체이므로 409(충돌) 던지기
		userRepository.findByUsername(userDto.getUsername()).ifPresent(duplicatedUser -> {
			throw new DuplicatedExcpetion("username: " + userDto.getUsername() + "은 이미 존재하는 회원입니다.");
		});
		
		// Dto에서 Entity로 변환
		User user = UserDto.toEntity(userDto, passwordEncoder);
		
		User createdUser = userRepository.save(user);
		
		return createdUser.getId();
	}
	
	// 전체 회원 목록 조회
	public List<User> readAllUsers() {
		return userRepository.findAll();
	}
	
	// userId로 회원 조회
	public User readUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new DataNotFoundException("회원을 찾을 수 없습니다."));
	}
	
	// username으로 회원 조회
	public User readUser(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new DataNotFoundException("username: " + username + " 회원을 찾을 수 없습니다."));
	}
	
	// 회원 수정
	@Transactional
	public void updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
		User user = readUser(userId);
		user.update(userUpdateRequest.getNickname(), userUpdateRequest.getEmail(), userUpdateRequest.getPhone());
		userRepository.save(user);
	}
	
	// 회원 삭제
	@Transactional
	public void deleteUser(Long userId) {
		User user = readUser(userId);
		userRepository.delete(user);
	}
	
	// 회원 1명이 작성한 게시글 목록 조회
	@Transactional
	public List<Article> readArticlesByUserId(Long userId) {
		User user = readUser(userId);
		return articleRepository.findByUser(user);
	}
	
	// 회원 1명이 작성한 게시글 댓글 목록 조회
	@Transactional
	public List<ArticleComment> readArticleCommentsByUserId(Long userId) {
		User user = readUser(userId);
		return articleCommentRepository.findByUser(user);
	}
	
	// 회원 1명이 작성한 콘서트 댓글 목록 조회
	@Transactional
	public List<ConcertComment> readConcertCommentsByUserId(Long userId) {
		User user = readUser(userId);
		return concertCommentRepository.findByUser(user);
	}
	
	// 회원 1명의 콘서트 찜 목록 조회
	@Transactional
	public List<ConcertWishlist> readConcertWishlistsByUserId(Long userId) {
		User user = readUser(userId);
		return concertWishlistRepository.findByUser(user);
	}
	
	// 회원 1명의 아티스트 찜 목록 조회
	@Transactional
	public List<ArtistWishlist> readArtistWishlistsByUserId(Long userId) {
		User user = readUser(userId);
		return artistWishlistRepository.findByUser(user);
	}
	
	// 회원 1명의 예매 목록 조회
	@Transactional
	public List<Reservation> readReservationsByUserId(Long userId) {
		User user = readUser(userId);
		return reservationRepository.findByUser(user);
	}
}





