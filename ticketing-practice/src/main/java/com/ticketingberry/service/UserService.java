package com.ticketingberry.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ticketingberry.model.entity.User;
import com.ticketingberry.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// 회원 가입
	public User addUser(String username, String password, String nickname, String email, 
						String phone, String birth, String gender, String role) {
		User user = new User();
		user.setUsername(username);
		user.setNickname(nickname);
		user.setEmail(email);
		user.setPhone(phone);
		user.setBirth(birth);
		user.setGender(gender);
		user.setRole(role);
		// password를 그냥 넣지 말고 암호화 시켜서 넣어줘야 함
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
		return user;
	}
	
	// 아이디로 회원 찾기
	public User getUser(String username) throws DataNotFoundException {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		User user = null;
		if (optionalUser != null) {
			user = optionalUser.get();
		} else {
			throw new DataNotFoundException("user not found");
		}
		return user;
	}
}













