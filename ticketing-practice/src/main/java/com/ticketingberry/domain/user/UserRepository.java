package com.ticketingberry.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);		// 한 개의 username에 해당하는 회원 한 명 찾기
}
