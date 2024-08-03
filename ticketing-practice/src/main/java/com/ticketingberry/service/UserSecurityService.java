package com.ticketingberry.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ticketingberry.domain.entity.User;
import com.ticketingberry.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import static com.ticketingberry.domain.UserRole.*;

@Service
@RequiredArgsConstructor
// UserDetailsService: DB에서 인증정보 획득
public class UserSecurityService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	// 회원 가입된 username으로 권한 얻어오기
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		@SuppressWarnings("serial")
		User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("username: " + username + " 회원을 찾을 수 없습니다.") {});
		
		// 자바 내장 클래스 GrantedAutority 자료형으로 리스트를 만듦
		// UserRole에서 권한(ADMIN or USER)을 얻어와서 넣기 위한 용도
		List<GrantedAuthority> authorities = new ArrayList<>();

		switch(user.getRole()) {
			case ADMIN -> {
				// 관리자(ADMIN) 권한을 얻어서 내장 권한 클래스 리스트에 넣음
				authorities.add(new SimpleGrantedAuthority(ADMIN.getRole()));	// 관리자 권한 부여
			}
			case USER -> {
				// 회원(USER) 권한을 얻어서 내장 권한 클래스 리스트에 넣음
				authorities.add(new SimpleGrantedAuthority(USER.getRole()));	// 회원 권한 부여
			}
		}
		
		// 자바 내장 클래스 User
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}
}
