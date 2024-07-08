package com.ticketingberry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
	
}
