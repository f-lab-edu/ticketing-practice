package com.ticketingberry.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ticketingberry.domain.entity.*;
import com.ticketingberry.domain.repository.*;
import com.ticketingberry.dto.UserDto;
import com.ticketingberry.exception.DataNotFoundException;
import com.ticketingberry.exception.DuplicatedException;
import com.ticketingberry.exception.PasswordsDoNotMatchException;

@ExtendWith(MockitoExtension.class)	// Mockito를 사용하여 단위 테스트를 수행
public class UserServiceTest {
	// 역할: 특정 클래스의 목(mock) 객체를 생성
	// 사용 위치: 테스트에서 사용되는 의존 객체에 대해 사용
	// 목적: 목 객체를 통해 실제 객체의 동작을 시뮬레이션하고, 특정 메서드 호출에 대해 정의된 동작을 수행하도록 함
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private ArticleRepository articleRepository;
	
	@Mock
	private ArticleCommentRepository articleCommentRepository;
	
	@Mock
	private ConcertCommentRepository concertCommentRepository;
	
	@Mock
	private ConcertWishlistRepository concertWishlistRepository;
	
	@Mock
	private ArtistWishlistRepository artistWishlistRepository;
	
	@Mock
	private ReservationRepository reservationRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	// 역할: 테스트 대상 객체를 생성하고, 이 객체의 의존성을 자동으로 주입
	// 사용 위치: 테스트 대상 클래스에 대해 사용
	// 목적: 의존성이 있는 객체를 주입받아, 테스트 대상 클래스의 실제 동작을 검증할 수 있도록 함
	@InjectMocks
	private UserService userService;
	
	private User user;
	
	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
	}
	
	@Test
	@DisplayName("회원 생성 성공")
	void createUser_success() {
		// 회원 가입 사용자 정보 설정
		UserDto userDto = new UserDto();
		userDto.setUsername("newUser");
		userDto.setPassword1("password");
		userDto.setPassword2("password");
		
		// 목 객체의 동작 정의
		when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(userDto.getPassword1())).thenReturn("encodedPassword");
		
		// 목 객체가 반환할 사용자 설정
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		// 서비스 메서드 호출 및 결과 검증
		Long userId = userService.createUser(userDto);
		
		// 결과가 예상대로인지 검증
		assertEquals(1L, userId);
		
		// userRepository.save()가 정확히 한 번 호출되었는지 검증
		verify(userRepository, times(1)).save(any(User.class));
	}
	
	@Test
	@DisplayName("회원 생성: 비밀번호와 비밀번호 확인이 다르면 PasswordsDoNotMatchException 던지기")
	void createUser_whenPasswordsDoNotMatch_throwsPasswordsDoNotMatchException() {
		UserDto userDto = new UserDto();
		userDto.setPassword1("password");
		userDto.setPassword2("differentPassword");
		
		PasswordsDoNotMatchException exception = assertThrows(PasswordsDoNotMatchException.class, 
											() -> userService.createUser(userDto));
	
		assertEquals("비밀번호와 비밀번호 확인이 다릅니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("회원 생성: username으로 회원 조회가 된 경우 중복 객체이므로 DuplicatedException 던지기")
	void createUser_whenUsernameExists_throwsDuplicatedException() {
		UserDto userDto = new UserDto();
		userDto.setUsername("existingUser");
		userDto.setPassword1("password");
		userDto.setPassword2("password");
		
		when (userRepository.findByUsername(userDto.getUsername()))
		.thenReturn(Optional.of(new User()));
		
		DuplicatedException exception = assertThrows(DuplicatedException.class,
										() -> userService.createUser(userDto));
		
		assertEquals("username: existingUser은 이미 존재하는 회원입니다.", exception.getMessage());;
	}
	
	@Test
	@DisplayName("userId로 회원 조회 성공")
	void readUser_success() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		
		User foundUser = userService.readUser(1L);
		
		assertEquals(user, foundUser);
	}
	
	@Test
	@DisplayName("userId로 회원 조회: userId로 회원 조회가 안 된 경우 DataNotFoundException 던지기")
	void readUser_whenUserIdDoesNotExist_throwsDataNotFoundException() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		
		DataNotFoundException exception = assertThrows(DataNotFoundException.class,
										  () -> userService.readUser(1L));
	
		assertEquals("회원을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("userId로 회원 조회: username으로 회원 조회가 안 된 경우 DataNotFoundException 던지기")
	void readUser_whenUsernameDoesNotExist_throwsDataNotFoundException() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
		
		DataNotFoundException exception = assertThrows(DataNotFoundException.class,
										  () -> userService.readUser("testuser"));
	
		assertEquals("username: testuser 회원을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("회원 1명이 작성한 게시글 목록 조회 성공")
	void readArticlesByUserId_success() {
		Article article1 = new Article();
		article1.setUser(user);
		Article article2 = new Article();
		article2.setUser(user);
		
		List<Article> articles = List.of(article1, article2);
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(articleRepository.findByUser(user)).thenReturn(articles);
		
		List<Article> result = userService.readArticlesByUserId(1L);
		
		assertEquals(articles, result);
		verify(userRepository, times(1)).findById(1L);
		verify(articleRepository, times(1)).findByUser(user);
	}
	
	@Test
	@DisplayName("회원 1명이 작성한 게시글 댓글 목록 조회 성공")
	void readArticleCommentsByUserId_success() {
		ArticleComment articleComment1 = new ArticleComment();
		articleComment1.setUser(user);
		ArticleComment articleComment2 = new ArticleComment();
		articleComment1.setUser(user);
		
		List<ArticleComment> articleComments = List.of(articleComment1, articleComment2);
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(articleCommentRepository.findByUser(user)).thenReturn(articleComments);
		
		List<ArticleComment> result = userService.readArticleCommentsByUserId(1L);
		
		assertEquals(articleComments, result);
		verify(userRepository, times(1)).findById(1L);
		verify(articleCommentRepository, times(1)).findByUser(user);
	}
	
	@Test
	@DisplayName("회원 1명이 작성한 콘서트 댓글 목록 조회 성공")
	void readConcertCommentsByUserId_success() {
		ConcertComment concertComment1 = new ConcertComment();
		concertComment1.setUser(user);
		ConcertComment concertComment2 = new ConcertComment();
		concertComment2.setUser(user);
		
		List<ConcertComment> concertComments = List.of(concertComment1, concertComment2);
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(concertCommentRepository.findByUser(user)).thenReturn(concertComments);
		
		List<ConcertComment> result = userService.readConcertCommentsByUserId(1L);
		
		assertEquals(concertComments, result);
		verify(userRepository, times(1)).findById(1L);
		verify(concertCommentRepository, times(1)).findByUser(user);
	}
	
	@Test
	@DisplayName("회원 1명의 콘서트 찜 목록 조회 성공")
	void readConcertWishlistsByUserId_success() {
		ConcertWishlist concertWishlist1 = new ConcertWishlist();
		concertWishlist1.setUser(user);
		ConcertWishlist concertWishlist2 = new ConcertWishlist();
		concertWishlist2.setUser(user);
		
		List<ConcertWishlist> concertWishlists = List.of(concertWishlist1, concertWishlist2);
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(concertWishlistRepository.findByUser(user)).thenReturn(concertWishlists);
		
		List<ConcertWishlist> result = userService.readConcertWishlistsByUserId(1L);
		
		assertEquals(concertWishlists, result);
		verify(userRepository, times(1)).findById(1L);
		verify(concertWishlistRepository, times(1)).findByUser(user);
	}
	
	@Test
	@DisplayName("회원 1명의 아티스트 찜 목록 조회 성공")
	void readArtistWishlistsByUserId_success() {
		ArtistWishlist artistWishlist1 = new ArtistWishlist();
		artistWishlist1.setUser(user);
		ArtistWishlist artistWishlist2 = new ArtistWishlist();
		artistWishlist2.setUser(user);
		
		List<ArtistWishlist> artistWishlists = List.of(artistWishlist1, artistWishlist2);
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(artistWishlistRepository.findByUser(user)).thenReturn(artistWishlists);
		
		List<ArtistWishlist> result = userService.readArtistWishlistsByUserId(1L);
		
		assertEquals(artistWishlists, result);
		verify(userRepository, times(1)).findById(1L);
		verify(artistWishlistRepository, times(1)).findByUser(user);
	}
	
	@Test
	@DisplayName("회원 1명의 예매 목록 조회 성공")
	void readReservationsByUserId_success() {
		Reservation reservation1 = new Reservation();
		reservation1.setUser(user);
		Reservation reservation2 = new Reservation();
		reservation2.setUser(user);
		
		List<Reservation> reservations = List.of(reservation1, reservation2);
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(reservationRepository.findByUser(user)).thenReturn(reservations);
		
		List<Reservation> result = userService.readReservationsByUserId(1L);
		
		assertEquals(reservations, result);
		verify(userRepository, times(1)).findById(1L);
		verify(reservationRepository, times(1)).findByUser(user);
	}
}
