package com.ticketingberry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	
}
