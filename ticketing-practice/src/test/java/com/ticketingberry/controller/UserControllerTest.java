package com.ticketingberry.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.*;

import static com.ticketingberry.domain.UserRole.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketingberry.controller.common.AbstractRestDocsTests;
import com.ticketingberry.domain.entity.*;
import com.ticketingberry.dto.UserDto;
import com.ticketingberry.exception.ExceptionAdvice;
import com.ticketingberry.exception.custom.DataDuplicatedException;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.exception.custom.PasswordsUnequalException;
import com.ticketingberry.dto.UpdateUserDto;
import com.ticketingberry.service.UserService;

//앞에서 작성한 API에 대해 테스트 코드를 작성한다.
//원래 REST Docs 적용을 위해서는 andDo()를 사용하여 field에 대한 내용을 채워줘야하지만
//상속받은 AbstractRestDocsTests에서 BeforeEach로 .alwaysDo(restDocs) 설정을 해주었기 때문에
//RestDocumentationResultHandler가 알아서 실제 응답에 따라 API를 생성해준다.
//하지만 이렇게 하면 description과 type에 대한 내용은 명시할 수 없기 때문에 필요에 따라 잘 사용하면 될 것 같다.

@WebMvcTest({UserController.class, ExceptionAdvice.class})
public class UserControllerTest extends AbstractRestDocsTests {
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private UserService userService;
	
	private User user;
	
	private UserDto userDto;
	
	@BeforeEach
	void setUp(WebApplicationContext context) {
		user = User.builder()
				.id(1L)
				.username("testuser")
				.password("testpassword")
				.nickname("테스트")
				.email("testuser@example.com")
				.phone("01012345678")
				.birth("20000101")
				.gender("F")
				.role(USER)
				.createdAt(LocalDateTime.now())
				.build();
		
		userDto = UserDto.builder()
				.username("testuser")
				.password1("testpassword")
				.password2("testpassword")
				.nickname("테스트")
				.email("testuser@example.com")
				.phone("01012345678")
				.birth("20000101")
				.gender("F")
				.build();
	}
	
	@Test
	@DisplayName("회원 가입")
	void addUser() throws Exception {
		// UserService의 Mock 객체에 대해 create 메서드 호출 시 반환 값 설정
		when(userService.create(any(UserDto.class))).thenReturn(user);
		
		// API 호출 및 테스트
		MvcResult result = mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDto)))
				.andExpect(status().isCreated())
				.andReturn();
		
		// 응답 본문을 문자열로 가져옴
		String responseBody = result.getResponse().getContentAsString();
		
		// JSON 문자열을 UserDto 객체로 변환
		UserDto responseUserDto = objectMapper.readValue(responseBody, UserDto.class);
		
		// 가져온 데이터에 대한 검증을 추가
		assertNotNull(responseUserDto);
		assertEquals(responseUserDto.getUsername(), userDto.getUsername());
	}
	
	@Test
	@DisplayName("회원 가입: 비밀번호와 비밀번호 확인이 다르면 400(BAD_REQUEST) 응답")
	void addUser_whenPasswordsDoNotMatch_throwsBadRequest() throws Exception {
		userDto = UserDto.builder()
				.username("testuser")
				.password1("password")
				.password2("differentPassword")
				.nickname("테스트")
				.email("testuser@example.com")
				.phone("01012345678")
				.birth("20000101")
				.gender("F")
				.build();
		
		Mockito.doThrow(new PasswordsUnequalException("비밀번호와 비밀번호 확인이 다릅니다."))
		.when(userService).create(any(UserDto.class));
		
		// API 호출 및 테스트
		mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("비밀번호와 비밀번호 확인이 다릅니다.")))
				.andExpect(jsonPath("$.status", is(400)));
	}
	
	@Test
	@DisplayName("회원 가입: username으로 회원 조회가 된 경우 중복 객체이므로 409(CONFLICT) 응답")
	void addUser_whenUsernameAlreadyExists_throwsConflict() throws Exception {		
		Mockito.doThrow(new DataDuplicatedException("username: <" + userDto.getUsername() + ">은 이미 존재하는 회원입니다."))
		.when(userService).create(any(UserDto.class));
		
		// API 호출 및 테스트
		mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDto)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message", is("username: <" + userDto.getUsername() + ">은 이미 존재하는 회원입니다.")))
				.andExpect(jsonPath("$.status", is(409)));
				
	}
	
	@Test
	@DisplayName("전체 회원 목록 조회")
	void getAllUsers() throws Exception {
		List<User> users = List.of(user,
				User.builder().id(2L).username("testuser2").password("testpassword")
				.nickname("테스트2").email("testuser2@example.com").phone("01098765432")
				.birth("20010101").gender("M").role(USER)
				.createdAt(LocalDateTime.now()).build());
		
		// UserService의 Mock 객체에 대해 findAll 메서드 호출 시 반환 값 설정
		when(userService.findAll()).thenReturn(users);
		
		// API 호출 및 테스트
		MvcResult result = mockMvc.perform(get("/api/users")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		// 응답 본문을 문자열로 가져옴
		String responseBody = result.getResponse().getContentAsString();
		
		// JSON 문자열을 리스트로 변환
		List<UserDto> responseUserDtos
			= objectMapper.readValue(responseBody, new TypeReference<List<UserDto>>() {});
		
		// 가져온 데이터에 대한 검증을 추가
		assertFalse(responseUserDtos.isEmpty());	// 사용자 리스트가 비어있지 않은지 확인
		assertEquals(responseUserDtos.get(0).getUsername(), userDto.getUsername());	// 첫 번째 사용자의 username 검증
		
		// 문서화는 상위 클래스에서 자동으로 처리됨
	}
	
	@Test
	@DisplayName("회원 1명 조회")
	void getUser() throws Exception {
		Long userId = 1L;
		
		// UserService의 Mock 객체에 대해 findById 메서드 호출 시 반환 값 설정
		when(userService.findById(userId)).thenReturn(user);
		
		MvcResult result = mockMvc.perform(get("/api/users/{userId}", userId))
				.andExpect(status().isOk())
				.andReturn();
		
		// 응답 본문을 문자열로 가져옴
		String responseBody = result.getResponse().getContentAsString();
		
		// JSON 문자열을 User 객체로 변환
		UserDto responseUserDto = objectMapper.readValue(responseBody, UserDto.class);
		
		// 가져온 데이터에 대한 검증을 추가
		assertNotNull(responseUserDto);	// 사용자가 null이 아닌지 확인
		assertEquals(responseUserDto.getUsername(), userDto.getUsername());	// 예시: 사용자의 username 검증
		
		// 문서화는 상위 클래스에서 자동으로 처리됨
	}
	
	@Test
	@DisplayName("회원 1명 조회: userId로 회원 조회가 안 된 경우 404(NOT_FOUND) 던지기")
	void getUser_whenUserIdDoesNotExist_throwsNotFound() throws Exception {
		Long userId = 1L;
		
		Mockito.when(userService.findById(userId))
		.thenThrow(new DataNotFoundException("회원을 찾을 수 없습니다."));
		
		mockMvc.perform(get("/api/users/{userId}", userId))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("회원을 찾을 수 없습니다.")))
				.andExpect(jsonPath("$.status", is(404)));
	}
	
	@Test
	@DisplayName("회원 정보 수정")
	void modifyUser() throws Exception {
		Long userId = 1L;
		
		UpdateUserDto updateUserDto = UpdateUserDto.builder()
				.nickname("닉네임변경")
				.email("changeEmail@example.com")
				.phone("01012345678")
				.build();
		
		user.update(updateUserDto.getNickname(), updateUserDto.getEmail(), updateUserDto.getPhone());
		
		when(userService.update(eq(userId), any(UpdateUserDto.class))).thenReturn(user);
		
		MvcResult result = mockMvc.perform(put("/api/users/{userId}", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateUserDto)))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		UserDto responseUserDto = objectMapper.readValue(responseBody, UserDto.class);
		
		assertNotNull(responseUserDto);
		assertEquals(responseUserDto.getNickname(), user.getNickname());
		assertEquals(responseUserDto.getEmail(), user.getEmail());
		assertEquals(responseUserDto.getPhone(), user.getPhone());
	}
	
	@Test
	@DisplayName("회원 탈퇴")
	void removeUser() throws Exception {
		Long userId = 1L;
		
		when(userService.delete(userId)).thenReturn(user);
		
		MvcResult result = mockMvc.perform(delete("/api/users/{userId}", userId))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		UserDto responseUserDto = objectMapper.readValue(responseBody, UserDto.class);
		
		assertNotNull(responseUserDto);
		assertEquals(responseUserDto.getUsername(), userDto.getUsername());
	}
	
//	@Test
//	@DisplayName("회원 1명이 작성한 게시글 목록 조회")
//	void getArticlesByUserId() throws Exception {		
//		Long userId = 1L;
//		
//		Board board = Board.builder().id(1L).name("자유게시판").build();
//		
//		List<Article> articles = List.of(
//					Article.builder().id(1L).board(board).user(user)
//					.title("제목").content("게시글의 내용입니다.").hits(0)
//					.createdAt(LocalDateTime.now()).build(),
//					Article.builder().id(2L).board(board).user(user)
//					.title("제목2").content("게시글2의 내용입니다.").hits(0)
//					.createdAt(LocalDateTime.now()).build());
//		
//		when(userService.findUserByUserId(userId)).thenReturn(user);
//		when(userService.findArticlesByUserId(userId)).thenReturn(articles);
//		
//		MvcResult result = mockMvc.perform(get("/api/users/{userId}/articles", userId)
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		// 응답 본문을 문자열로 가져옴
//		String responseBody = result.getResponse().getContentAsString();
//		
//		// JSON 문자열을 리스트로 변환
//		List<Article> articleList = objectMapper.readValue(responseBody, new TypeReference<List<Article>>() {});
//		
//		// 가져온 데이터에 대한 검증을 추가
//		assertFalse(articleList.isEmpty());	// 게시글 리스트가 비어있지 않은지 확인
//		assertEquals("제목", articleList.get(0).getTitle());	// 게시글의 제목 검증
//		assertEquals("테스트", articleList.get(0).getUser().getNickname());	// 게시글 작성자 검증
//		assertEquals("자유게시판", articleList.get(0).getBoard().getName());	// 게시글이 속한 게시판 검증
//	}
//	
//	@Test
//	@DisplayName("회원 1명이 작성한 게시글 댓글 목록 조회")
//	void getArticleCommentsByUserId() throws Exception {	
//		Long userId = 1L;
//		
//		Board board = Board.builder().id(1L).name("자유게시판").build();
//		
//		Article article = Article.builder().id(1L).board(board).user(user).title("제목")
//				.content("게시글의 내용입니다.").createdAt(LocalDateTime.now()).build();
//		
//		List<ArticleComment> articleComments = List.of(
//				ArticleComment.builder().id(1L).article(article).user(user)
//				.content("게시글 댓글의 내용입니다.").createdAt(LocalDateTime.now()).build(),
//				ArticleComment.builder().id(2L).article(article).user(user)
//				.content("게시글 댓글2의 내용입니다.").createdAt(LocalDateTime.now()).build());
//		
//		when(userService.findUserByUserId(userId)).thenReturn(user);
//		when(userService.findArticleCommentsByUserId(userId)).thenReturn(articleComments);
//		
//		MvcResult result = mockMvc.perform(get("/api/users/{userId}/article-comments", userId)
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		// 응답 본문을 문자열로 가져옴
//		String responseBody = result.getResponse().getContentAsString();
//		
//		// JSON 문자열을 리스트로 변환
//		List<ArticleComment> articleCommentList = objectMapper.readValue(
//				responseBody, new TypeReference<List<ArticleComment>>() {});
//		
//		// 가져온 데이터에 대한 검증을 추가
//		assertFalse(articleCommentList.isEmpty());	// 게시글 댓글 리스트가 비어있지 않은지 확인
//		assertEquals("테스트", articleCommentList.get(0).getUser().getNickname());	// 게시글 댓글 작성자 검증
//		assertEquals("제목", articleCommentList.get(0).getArticle().getTitle());	// 댓글이 속한 게시글 제목 검증
//		assertEquals("게시글 댓글의 내용입니다.", articleCommentList.get(0).getContent());	// 게시글 댓글 내용 검증
//	}
//	
//	@Test
//	@DisplayName("회원 1명이 작성한 콘서트 댓글 목록 조회")
//	void getConcertCommentsByUserId() throws Exception {
//		Long userId = 1L;
//		
//		Place place = Place.builder().id(1L).name("KSPO DOME")
//				.createdAt(LocalDateTime.now()).build();
//		
//		Artist artist = Artist.builder().id(1L).name("아이유")
//				.createdAt(LocalDateTime.now()).build();
//		
//		Concert concert = Concert.builder().id(1L).place(place).artist(artist)
//				.title("IU Concert").content("아이유 콘서트의 내용입니다.").hits(0)
//				.performedAt(LocalDateTime.of(2024, Month.FEBRUARY, 25, 17, 00))
//				.createdAt(LocalDateTime.now()).build();
//		
//		List<ConcertComment> concertComments = List.of(
//				ConcertComment.builder().id(1L).concert(concert).user(user)
//				.content("콘서트의 댓글입니다.").createdAt(LocalDateTime.now()).build(),
//				ConcertComment.builder().id(2L).concert(concert).user(user)
//				.content("콘서트의 댓글2입니다.").createdAt(LocalDateTime.now()).build());
//		
//		when(userService.findUserByUserId(userId)).thenReturn(user);
//		when(userService.findConcertCommentsByUserId(userId)).thenReturn(concertComments);
//		
//		MvcResult result = mockMvc.perform(get("/api/users/{userId}/concert-comments", userId)
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		// 응답 본문을 문자열로 가져옴
//		String responseBody = result.getResponse().getContentAsString();
//		
//		// JSON 문자열을 리스트로 변환
//		List<ConcertComment> concertCommentList = objectMapper.readValue(
//				responseBody, new TypeReference<List<ConcertComment>>() {});
//		
//		// 가져온 데이터에 대한 검증을 추가
//		assertFalse(concertCommentList.isEmpty());	// 콘서트 댓글 리스트가 비어있지 않은지 확인
//		assertEquals("콘서트의 댓글입니다.", concertCommentList.get(0).getContent());	// 콘서트 댓글 내용 검증
//		assertEquals("테스트", concertCommentList.get(0).getUser().getNickname());	// 콘서트 댓글 작성자 검증
//		assertEquals("IU Concert", concertCommentList.get(0).getConcert().getTitle());	// 댓글이 속한 콘서트 제목 검증
//	}
//	
//	@Test
//	@DisplayName("회원 1명의 콘서트 찜 목록 조회")
//	void getConcertWishlistsByUserId() throws Exception {
//		Long userId = 1L;
//		
//		Place place = Place.builder().id(1L).name("KSPO DOME")
//				.createdAt(LocalDateTime.now()).build();
//		
//		Artist artist = Artist.builder().id(1L).name("아이유")
//				.createdAt(LocalDateTime.now()).build();
//		
//		Concert concert = Concert.builder().id(1L).place(place).artist(artist)
//				.title("IU Concert").content("아이유 콘서트의 내용입니다.").hits(0)
//				.performedAt(LocalDateTime.of(2024, Month.FEBRUARY, 25, 17, 00))
//				.createdAt(LocalDateTime.now()).build();
//		
//		List<ConcertWishlist> concertWishlists = List.of(
//				ConcertWishlist.builder().id(1L).concert(concert).user(user)
//				.createdAt(LocalDateTime.now()).build());
//		
//		when(userService.findUserByUserId(userId)).thenReturn(user);
//		when(userService.findConcertWishlistsByUserId(userId)).thenReturn(concertWishlists);
//		
//		MvcResult result = mockMvc.perform(get("/api/users/{userId}/concert-wishlists", userId)
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		// 응답 본문을 문자열로 가져옴
//		String responseBody = result.getResponse().getContentAsString();
//		
//		// JSON 문자열을 리스트로 변환
//		List<ConcertWishlist> concertWishlistList = objectMapper.readValue(
//				responseBody, new TypeReference<List<ConcertWishlist>>() {});
//		
//		// 가져온 데이터에 대한 검증을 추가
//		assertFalse(concertWishlistList.isEmpty());	// 콘서트 찜 리스트가 비어있지 않은지 확인
//		assertEquals("테스트", concertWishlistList.get(0).getUser().getNickname());	// 콘서트 찜의 소유자 검증
//		assertEquals("IU Concert", concertWishlistList.get(0).getConcert().getTitle());	// 찜한 콘서트 제목 검증
//	}
//	
//	@Test
//	@DisplayName("회원 1명의 아티스트 찜 목록 조회")
//	void getArtistWishlistsByUserId() throws Exception {
//		Long userId = 1L;
//		
//		Artist artist = Artist.builder().id(1L).name("아이유")
//				.createdAt(LocalDateTime.now()).build();
//		
//		List<ArtistWishlist> artistWishlists = List.of(
//				ArtistWishlist.builder().id(1L).artist(artist).user(user)
//				.createdAt(LocalDateTime.now()).build());
//		
//		when(userService.findUserByUserId(userId)).thenReturn(user);
//		when(userService.findArtistWishlistsByUserId(userId)).thenReturn(artistWishlists);
//		
//		MvcResult result = mockMvc.perform(get("/api/users/{userId}/artist-wishlists", userId)
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		// 응답 본문을 문자열로 가져옴
//		String responseBody = result.getResponse().getContentAsString();
//		
//		// JSON 문자열을 리스트로 변환
//		List<ArtistWishlist> artistWishlistList = objectMapper.readValue(
//				responseBody, new TypeReference<List<ArtistWishlist>>() {});
//		
//		// 가져온 데이터에 대한 검증을 추가
//		assertFalse(artistWishlistList.isEmpty());	// 아티스트 찜 리스트가 비어있지 않은지 확인
//		assertEquals("테스트", artistWishlistList.get(0).getUser().getNickname());	// 아티스트 찜의 소유자 검증
//		assertEquals("아이유", artistWishlistList.get(0).getArtist().getName());	// 찜한 아티스트 검증
//	}
//	
//	@Test
//	@DisplayName("회원 1명이 예매한 공연 목록 조회")
//	void getReservationsByUserId() throws Exception {
//		Long userId = 1L;
//		
//		Place place = Place.builder().id(1L).name("KSPO DOME")
//				.createdAt(LocalDateTime.now()).build();
//		
//		Artist artist = Artist.builder().id(1L).name("아이유")
//				.createdAt(LocalDateTime.now()).build();
//		
//		Concert concert = Concert.builder().id(1L).place(place).artist(artist)
//				.title("IU Concert").content("아이유 콘서트의 내용입니다.").hits(0)
//				.performedAt(LocalDateTime.of(2024, Month.FEBRUARY, 25, 17, 00))
//				.createdAt(LocalDateTime.now()).build();
//		
//		District district = District.builder().id(1L).concert(concert)
//				.districtName("A").createdAt(LocalDateTime.now()).build();
//		
//		Seat seat = Seat.builder().id(1L).district(district).rowNum(1)
//				.seatNum(1).createdAt(LocalDateTime.now()).build();
//		
//		List<Reservation> reservations = List.of(
//				Reservation.builder().id(1L).seat(seat).user(user).deposited(true)
//				.createdAt(LocalDateTime.now()).build());
//	
//		when(userService.findUserByUserId(userId)).thenReturn(user);
//		when(userService.findReservationsByUserId(userId)).thenReturn(reservations);
//		
//		MvcResult result = mockMvc.perform(get("/api/users/{userId}/reservations", userId)
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		// 응답 본문을 문자열로 가져옴
//		String responseBody = result.getResponse().getContentAsString();
//		
//		// JSON 문자열을 리스트로 변환
//		List<Reservation> reservationList = objectMapper.readValue(
//				responseBody, new TypeReference<List<Reservation>>() {});
//		
//		// 가져온 데이터에 대한 검증을 추가
//		assertFalse(reservationList.isEmpty());		// 예약 리스트가 비어있지 않은지 확인
//		assertEquals("테스트", reservationList.get(0).getUser().getNickname());	// 예약의 소유자 검증
//		assertEquals(1, reservationList.get(0).getSeat().getSeatNum());			// 예약한 자리 번호 검증
//		assertEquals(1, reservationList.get(0).getSeat().getRowNum());			// 예약한 열 번호 검증
//		assertEquals("A", reservationList.get(0).getSeat().getDistrict().getDistrictName());		// 예약한 구역 이름 검증
//		assertEquals("IU Concert", reservationList.get(0).getSeat().getDistrict().getConcert().getTitle());	// 예약한 콘서트 이름 검증
//	}
//	
//	@Test
//	// 회원 1명이 예매한 공연 1개 취소
//	public void deleteReservationByUser() throws Exception {
//		Long userId = 1L;
//		Long reservationId = 1L;
//		
//		when(userService.readUser(userId)).thenReturn(user);
//		doNothing().when(reservationService).deleteReservation(reservationId);	// 예매 삭제 시 아무 동작도 하지 않도록 하기
//		
//		// API 호출 및 테스트
//		mockMvc.perform(delete("/api/users/{userId}/reservations/{reservationId}", 1, 1))
//				.andExpect(status().isOk())
//				.andExpect(content().string(reservationId.toString()));
//	}
}
