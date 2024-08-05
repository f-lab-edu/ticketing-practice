package com.ticketingberry.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticketingberry.domain.entity.Place;
import com.ticketingberry.domain.repository.PlaceRepository;
import com.ticketingberry.dto.PlaceDto;
import com.ticketingberry.exception.custom.DataNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {
	private final PlaceRepository placeRepository;
	
	// 공연 장소 추가
	@Transactional
	public Long createPlace(PlaceDto placeDto) {
		Place place = Place.builder()
				.name(placeDto.getName())
				.build();
		Place createdPlace = placeRepository.save(place);
		return createdPlace.getId();
	}
	
	// 전체 공연 장소 목록 조회
	@Transactional
	public List<Place> findAllPlaces() {
		return placeRepository.findAll();
	}
	
	// 공연 1개 조회
	@Transactional
	public Place findPlaceById(Long placeId) {
		return placeRepository.findById(placeId)
				.orElseThrow(() -> new DataNotFoundException("장소를 찾을 수 없습니다."));
	}
	
	// 공연 장소 수정
	@Transactional
	public void updatePlace(Long placeId, PlaceDto placeDto) {
		Place place = findPlaceById(placeId);
		place.update(placeDto.getName());
		placeRepository.save(place);
	}
	
	// 공연 장소 삭제
	@Transactional
	public void deletePlace(Long placeId) {
		Place place = findPlaceById(placeId);
		placeRepository.delete(place);
	}
}
