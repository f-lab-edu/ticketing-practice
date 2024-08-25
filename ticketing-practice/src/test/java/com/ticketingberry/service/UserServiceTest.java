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

import com.ticketingberry.domain.user.User;
import com.ticketingberry.domain.user.UserRepository;
import com.ticketingberry.dto.user.UserUpdateRequest;
import com.ticketingberry.dto.user.UserCreateRequest;
import com.ticketingberry.exception.custom.DataDuplicatedException;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.exception.custom.PasswordsUnequalException;

@ExtendWith(MockitoExtension.class)	// Mockito를 사용하여 단위 테스트를 수행
public class UserServiceTest {
	// 역할: 특정 클래스의 목(mock) 객체를 생성
	// 사용 위치: 테스트에서 사용되는 의존 객체에 대해 사용
	// 목적: 목 객체를 통해 실제 객체의 동작을 시뮬레이션하고, 특정 메서드 호출에 대해 정의된 동작을 수행하도록 함
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	// 역할: 테스트 대상 객체를 생성하고, 이 객체의 의존성을 자동으로 주입
	// 사용 위치: 테스트 대상 클래스에 대해 사용
	// 목적: 의존성이 있는 객체를 주입받아, 테스트 대상 클래스의 실제 동작을 검증할 수 있도록 함
	@InjectMocks
	private UserService userService;
	
	private User user;
	
	private UserCreateRequest userCreateRequest;
	
	private UserUpdateRequest userUpdateRequest;
	
	@BeforeEach
	void setUp() {
		user = User.builder()
				.id(1L)
				.username("testuser")
				.build();
		
		userCreateRequest = UserCreateRequest.builder()
				.username("testuser")
				.password1("testpassword")
				.password2("testpassword")
				.build();
				
		userUpdateRequest = UserUpdateRequest.builder()
				.nickname("닉네임변경")
				.email("changeEmail@example.com")
				.phone("01012345678")
				.build();
	}
	
	@Test
	@DisplayName("회원 생성 성공")
	void createUser_success() {
		// 목 객체의 동작 정의
		when(userRepository.findByUsername(userCreateRequest.getUsername())).thenReturn(Optional.empty());
		
		// 목 객체가 반환할 사용자 설정
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		// 서비스 메서드 호출 및 결과 검증
		User result = userService.create(userCreateRequest);
		
		// 결과가 예상대로인지 검증
		assertEquals(user, result);
	}
	
	@Test
	@DisplayName("회원 생성: 비밀번호와 비밀번호 확인이 다르면 PasswordsUnequalException 던지기")
	void createUser_whenPasswordsDoNotMatch_throwsPasswordsUnequalException() {
		userCreateRequest = UserCreateRequest.builder()
				.password1("password")
				.password2("differentPassword")
				.build();
		
		PasswordsUnequalException exception = assertThrows(PasswordsUnequalException.class, 
											() -> userService.create(userCreateRequest));
	
		assertEquals("비밀번호와 비밀번호 확인이 다릅니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("회원 생성: username으로 회원 조회가 된 경우 중복 객체이므로 DataDupliactedException 던지기")
	void createUser_whenUsernameExists_throwsDataDupliactedException() {
		when (userRepository.findByUsername(userCreateRequest.getUsername()))
		.thenReturn(Optional.of(User.builder().username("testuser").build()));
		
		DataDuplicatedException exception = assertThrows(DataDuplicatedException.class,
										() -> userService.create(userCreateRequest));
		
		assertEquals("username: <testuser>은 이미 존재하는 회원입니다.", exception.getMessage());;
	}
	
	@Test
	@DisplayName("전체 회원 목록 조회 성공")
	void readAllUsers_success() {
		List<User> users = List.of(user, User.builder().build());
		when (userRepository.findAll()).thenReturn(users);
		List<User> result = userService.findAll();
		assertEquals(users, result);
	}
	
	@Test
	@DisplayName("userId로 회원 조회 성공")
	void readUserById_success() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		User result = userService.findById(1L);
		assertEquals(user, result);
	}
	
	@Test
	@DisplayName("userId로 회원 조회: userId로 회원 조회가 안 된 경우 DataNotFoundException 던지기")
	void readUserById_whenUserIdDoesNotExist_throwsDataNotFoundException() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		
		DataNotFoundException exception = assertThrows(DataNotFoundException.class,
										  () -> userService.findById(1L));
	
		assertEquals("회원을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("username으로 회원 조회 성공")
	void readUserByUsername_success() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		User result = userService.findByUsername("testuser");
		assertEquals(user, result);
	}
	
	@Test
	@DisplayName("username으로 회원 조회: username으로 회원 조회가 안 된 경우 DataNotFoundException 던지기")
	void readUserByUsername_whenUsernameDoesNotExist_throwsDataNotFoundException() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
		
		DataNotFoundException exception = assertThrows(DataNotFoundException.class,
										  () -> userService.findByUsername("testuser"));
	
		assertEquals("username: <testuser> 회원을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("회원 수정 성공")
	void updateUser_success() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		User result = userService.update(1L, userUpdateRequest);
		
		assertEquals(result.getNickname(), userUpdateRequest.getNickname());
		assertEquals(result.getEmail(), userUpdateRequest.getEmail());
		assertEquals(result.getPhone(), userUpdateRequest.getPhone());
	}
	
	@Test
	@DisplayName("회원 삭제 성공")
	void deleteUser_success() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		User result = userService.delete(1L);
		verify(userRepository, times(1)).delete(user);
		assertEquals(user, result);
	}
}
