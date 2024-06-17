package com.ticketingberry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.model.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
