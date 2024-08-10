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
	public Place create(PlaceDto placeDto) {
		Place place = Place.of(placeDto);
		return placeRepository.save(place);
	}
	
	// 전체 공연 장소 목록 조회
	@Transactional
	public List<Place> findAll() {
		return placeRepository.findAll();
	}
	
	// 공연 1개 조회
	@Transactional
	public Place findById(Long placeId) {
		return placeRepository.findById(placeId)
				.orElseThrow(() -> new DataNotFoundException("장소를 찾을 수 없습니다."));
	}
	
	// 공연 장소 수정
	@Transactional
	public Place update(Long placeId, PlaceDto placeDto) {
		Place place = findById(placeId);
		place.update(placeDto.getName());
		return placeRepository.save(place);
	}
	
	// 공연 장소 삭제
	@Transactional
	public Place delete(Long placeId) {
		Place place = findById(placeId);
		placeRepository.delete(place);
		return place;
	}
}
