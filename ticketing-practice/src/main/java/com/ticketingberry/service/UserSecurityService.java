package com.ticketingberry.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ticketingberry.model.UserRole;
import com.ticketingberry.model.entity.User;
import com.ticketingberry.repository.UserRepository;

@Service
// UserDetailsService: DB에서 인증정보 획득
public class UserSecurityService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		User user = optionalUser.get();
		
		// 자바 내장 클래스 GrantedAutority 자료형으로 리스트를 만듦
		// UserRole에서 권한(ADMIN or USER)을 얻어와서 넣기 위한 용도
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		if ("관리자".equals(user.getRole())) {
			// 관리자(ADMIN) 권한을 얻어서 내장 권한 클래스 리스트에 넣음
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getRole()));	// 관리자 권한 부여
		} else {
			// 회원(USER) 권한을 얻어서 내장 권한 클래스 리스트에 넣음
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getRole()));	// 회원 권한 부여
		}
		
		// 자바 내장 클래스 User
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}
}














