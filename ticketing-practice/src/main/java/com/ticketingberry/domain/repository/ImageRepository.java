package com.ticketingberry.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
