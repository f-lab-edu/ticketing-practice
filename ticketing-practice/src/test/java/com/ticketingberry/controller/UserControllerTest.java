package com.ticketingberry.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static com.ticketingberry.domain.user.UserRole.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ticketingberry.controller.common.AbstractRestDocsTests;
import com.ticketingberry.domain.user.User;
import com.ticketingberry.exception.ExceptionAdvice;
import com.ticketingberry.exception.custom.DataDuplicatedException;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.exception.custom.PasswordsUnequalException;
import com.ticketingberry.dto.user.UpdateUserDto;
import com.ticketingberry.dto.user.UserDto;
import com.ticketingberry.dto.user.InUserDto;
import com.ticketingberry.service.UserService;

//앞에서 작성한 API에 대해 테스트 코드를 작성한다.
//원래 REST Docs 적용을 위해서는 andDo()를 사용하여 field에 대한 내용을 채워줘야하지만
//상속받은 AbstractRestDocsTests에서 BeforeEach로 .alwaysDo(restDocs) 설정을 해주었기 때문에
//RestDocumentationResultHandler가 알아서 실제 응답에 따라 API를 생성해준다.
//하지만 이렇게 하면 description과 type에 대한 내용은 명시할 수 없기 때문에 필요에 따라 잘 사용하면 될 것 같다.

@WebMvcTest({UserController.class, ExceptionAdvice.class})
public class UserControllerTest extends AbstractRestDocsTests {
	@MockBean
	private UserService userService;
	
	private User user;
	
	private InUserDto userDto;
	
	@BeforeEach
	void setUp() {
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
		
		userDto = InUserDto.builder()
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
		when(userService.create(any(InUserDto.class))).thenReturn(user);
		
		// API 호출 및 테스트
		MvcResult result = mockMvc.perform(post("/users")
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
		userDto = InUserDto.builder()
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
		.when(userService).create(any(InUserDto.class));
		
		// API 호출 및 테스트
		mockMvc.perform(post("/users")
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
		.when(userService).create(any(InUserDto.class));
		
		// API 호출 및 테스트
		mockMvc.perform(post("/users")
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
		MvcResult result = mockMvc.perform(get("/users")
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
		
		MvcResult result = mockMvc.perform(get("/users/{userId}", userId))
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
	@DisplayName("회원 1명 조회: userId로 회원 조회가 안 된 경우 404(NOT_FOUND) 응답")
	void getUser_whenUserIdDoesNotExist_throwsNotFound() throws Exception {
		Long userId = 1L;
		
		when(userService.findById(userId))
		.thenThrow(new DataNotFoundException("회원을 찾을 수 없습니다."));
		
		mockMvc.perform(get("/users/{userId}", userId))
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
		
		MvcResult result = mockMvc.perform(put("/users/{userId}", userId)
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
		
		MvcResult result = mockMvc.perform(delete("/users/{userId}", userId))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		UserDto responseUserDto = objectMapper.readValue(responseBody, UserDto.class);
		
		assertNotNull(responseUserDto);
		assertEquals(responseUserDto.getUsername(), userDto.getUsername());
	}
}
