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
import com.ticketingberry.domain.repository.common.UserRelatedRepository;
import com.ticketingberry.dto.UserDto;
import com.ticketingberry.dto.UpdateUserDto;
import com.ticketingberry.exception.DataNotFoundException;
import com.ticketingberry.exception.DuplicatedException;
import com.ticketingberry.exception.PasswordsDoNotMatchException;

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
		// 비밀번호와 비밀번호 확인이 다를 경우 400(BAD_REQUEST) 던지기
		if (!userDto.getPassword1().equals(userDto.getPassword2())) {
			throw new PasswordsDoNotMatchException("비밀번호와 비밀번호 확인이 다릅니다.");
		}
		
		// username으로 회원 조회가 된 경우 중복 객체이므로 409(CONFLICT) 던지기
		userRepository.findByUsername(userDto.getUsername()).ifPresent(duplicatedUser -> {
			throw new DuplicatedException("username: " + userDto.getUsername() + "은 이미 존재하는 회원입니다.");
		});
		
		// Dto에서 Entity로 변환
		User user = UserDto.toEntity(userDto, passwordEncoder);
		
		User createdUser = userRepository.save(user);
		
		return createdUser.getId();
	}
	
	// 전체 회원 목록 조회
	@Transactional
	public List<User> readAllUsers() {
		return userRepository.findAll();
	}
	
	// userId로 회원 조회
	@Transactional
	public User readUserById(Long userId) {
		// userId로 회원 조회가 안 된 경우 404(NOT_FOUND) 던지기
		return userRepository.findById(userId)
				.orElseThrow(() -> new DataNotFoundException("회원을 찾을 수 없습니다."));
	}
	
	// username으로 회원 조회
	@Transactional
	public User readUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new DataNotFoundException("username: " + username + " 회원을 찾을 수 없습니다."));
	}
	
	// 회원 수정
	@Transactional
	public void updateUser(Long userId, UpdateUserDto updateUserDto) {
		User user = readUserById(userId);
		user.update(updateUserDto.getNickname(), updateUserDto.getEmail(), updateUserDto.getPhone());
		userRepository.save(user);
	}
	
	// 회원 삭제
	@Transactional
	public void deleteUser(Long userId) {
		User user = readUserById(userId);
		userRepository.delete(user);
	}
	
	// 회원 1명의 엔티티 목록 조회
	@Transactional
	public <T> List<T> readEntitiesByUserId(Long userId, UserRelatedRepository<T> repository) {
		User user = readUserById(userId);
		return repository.findByUser(user);
	}
	
	// 회원 1명이 작성한 게시글 목록 조회
	@Transactional
	public List<Article> readArticlesByUserId(Long userId) {
		return readEntitiesByUserId(userId, articleRepository);
	}
	
	// 회원 1명이 작성한 게시글 댓글 목록 조회
	@Transactional
	public List<ArticleComment> readArticleCommentsByUserId(Long userId) {
		return readEntitiesByUserId(userId, articleCommentRepository);
	}
	
	// 회원 1명이 작성한 콘서트 댓글 목록 조회
	@Transactional
	public List<ConcertComment> readConcertCommentsByUserId(Long userId) {
		return readEntitiesByUserId(userId, concertCommentRepository);
	}
	
	// 회원 1명의 콘서트 찜 목록 조회
	@Transactional
	public List<ConcertWishlist> readConcertWishlistsByUserId(Long userId) {
		return readEntitiesByUserId(userId, concertWishlistRepository);
	}
	
	// 회원 1명의 아티스트 찜 목록 조회
	@Transactional
	public List<ArtistWishlist> readArtistWishlistsByUserId(Long userId) {
		return readEntitiesByUserId(userId, artistWishlistRepository);
	}
	
	// 회원 1명의 예매 목록 조회
	@Transactional
	public List<Reservation> readReservationsByUserId(Long userId) {
		return readEntitiesByUserId(userId, reservationRepository);
	}
}
