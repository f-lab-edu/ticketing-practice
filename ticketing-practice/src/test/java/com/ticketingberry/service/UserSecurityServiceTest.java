package com.ticketingberry.service;

import static com.ticketingberry.domain.user.UserRole.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ticketingberry.domain.user.User;
import com.ticketingberry.domain.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserSecurityServiceTest {
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserSecurityService userSecurityService;
	
	@Test
	@DisplayName("회원 가입된 username으로 ADMIN 권한 얻어오기")
	void loadUserByUsername_getRoleADMIN_success() {
		User user = User.builder()
				.username("testuser")
				.password("testpassword")
				.role(ADMIN)
				.build();
		
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		
		UserDetails userDetails = userSecurityService.loadUserByUsername("testuser");
		
		assertNotNull(userDetails);
		assertEquals(user.getUsername(), userDetails.getUsername());
		assertEquals(user.getPassword(), userDetails.getPassword());
		assertTrue(userDetails.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")));
		
		verify(userRepository, times(1)).findByUsername("testuser");
	}
	
	@Test
	@DisplayName("회원 가입된 username으로 USER 권한 얻어오기")
	void loadUserByUsername_getRoleUSER_success() {
		User user = User.builder()
				.username("testuser")
				.password("testpassword")
				.role(USER)
				.build();
		
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		
		UserDetails userDetails = userSecurityService.loadUserByUsername("testuser");
		
		assertNotNull(userDetails);
		assertEquals(user.getUsername(), userDetails.getUsername());
		assertEquals(user.getPassword(), userDetails.getPassword());
		assertTrue(userDetails.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));
		
		verify(userRepository, times(1)).findByUsername("testuser");
	}
	
	@Test
	@DisplayName("회원 가입된 username으로 권한 얻어오기: username으로 회원 조회가 안 된 경우 UsernameNotFoundException 던지기")
	void loadUserByUsername_whenUsernameDoesNotExist_throwsUsernameNotFoundException() {
		when(userRepository.findByUsername("nouser")).thenReturn(Optional.empty());
		
		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, 
											  () -> userSecurityService.loadUserByUsername("nouser"));
		
		verify(userRepository, times(1)).findByUsername("nouser");
		assertEquals("username: nouser 회원을 찾을 수 없습니다.", exception.getMessage());
	}
}
