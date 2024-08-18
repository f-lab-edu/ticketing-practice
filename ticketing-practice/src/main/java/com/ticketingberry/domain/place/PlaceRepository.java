package com.ticketingberry.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {
	
}
