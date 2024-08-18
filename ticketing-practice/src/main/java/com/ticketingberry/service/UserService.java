package com.ticketingberry.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticketingberry.domain.user.User;
import com.ticketingberry.domain.user.UserRepository;
import com.ticketingberry.exception.custom.DataDuplicatedException;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.exception.custom.PasswordsUnequalException;
import com.ticketingberry.dto.user.UpdateUserDto;
import com.ticketingberry.dto.user.InUserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor	// 'final'이 붙은 필드 중에 초기화 되지 않은 모든 필드를 인자로 설정한 생성자를 만들어 줌
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	// 회원 생성
	@Transactional
	public User create(InUserDto userDto) {
		// 비밀번호와 비밀번호 확인이 다를 경우 400(BAD_REQUEST) 던지기
		if (!userDto.getPassword1().equals(userDto.getPassword2())) {
			throw new PasswordsUnequalException("비밀번호와 비밀번호 확인이 다릅니다.");
		}
		
		// username으로 회원 조회가 된 경우 중복 객체이므로 409(CONFLICT) 던지기
		userRepository.findByUsername(userDto.getUsername()).ifPresent(duplicatedUser -> {
			throw new DataDuplicatedException("username: <" + duplicatedUser.getUsername() + ">은 이미 존재하는 회원입니다.");
		});
		
		// Dto에서 Entity로 변환
		User user = User.of(userDto, passwordEncoder);
		return userRepository.save(user);
	}
	
	// 전체 회원 목록 조회
	@Transactional
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	// userId로 회원 조회
	@Transactional
	public User findById(Long userId) {
		// userId로 회원 조회가 안 된 경우 404(NOT_FOUND) 던지기
		return userRepository.findById(userId)
				.orElseThrow(() -> new DataNotFoundException("회원을 찾을 수 없습니다."));
	}
	
	// username으로 회원 조회
	@Transactional
	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new DataNotFoundException("username: <" + username + "> 회원을 찾을 수 없습니다."));
	}
	
	// 회원 수정
	@Transactional
	public User update(Long userId, UpdateUserDto updateUserDto) {
		User user = findById(userId);
		user.update(updateUserDto.getNickname(), updateUserDto.getEmail(), updateUserDto.getPhone());
		return userRepository.save(user);
	}
	
	// 회원 삭제
	@Transactional
	public User delete(Long userId) {
		User user = findById(userId);
		userRepository.delete(user);
		return user;
	}
}
