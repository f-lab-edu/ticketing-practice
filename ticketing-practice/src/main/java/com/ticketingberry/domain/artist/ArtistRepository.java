package com.ticketingberry.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
	
}
