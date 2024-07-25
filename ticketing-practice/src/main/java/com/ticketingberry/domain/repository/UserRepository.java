package com.ticketingberry.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);		// 한 개의 username에 해당하는 회원 한 명 찾기
	boolean existsByUsername(String username);			// username에 해당하는 회원이 있는지의 여부
}
