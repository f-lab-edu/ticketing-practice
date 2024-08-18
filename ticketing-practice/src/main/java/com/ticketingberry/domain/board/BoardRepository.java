package com.ticketingberry.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	
}
