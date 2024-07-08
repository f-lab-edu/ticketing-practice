package com.ticketingberry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);		// 한 개의 아이디에 해당하는 회원 한 명 찾기
}
