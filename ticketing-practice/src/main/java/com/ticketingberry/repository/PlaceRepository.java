package com.ticketingberry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
	
}
