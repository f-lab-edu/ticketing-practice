package com.ticketingberry.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.domain.entity.Place;
import com.ticketingberry.dto.PlaceDto;
import com.ticketingberry.service.PlaceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {
	private final PlaceService placeService;
	
	// 공연 장소 추가
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String addPlace(@Valid @RequestBody PlaceDto placeDto) {
		Long placeId = placeService.createPlace(placeDto);
		return "장소(id: " + placeId + ") 추가에 성공했습니다.";
	}
	
	// 전체 공연 장소 목록 조회
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<PlaceDto> getAllPlaces() {
		List<Place> placeList = placeService.findAllPlaces();
		List<PlaceDto> placeDtoList = new ArrayList<>();
		
		for (Place place : placeList) {
			PlaceDto placeDto = PlaceDto.builder()
					.name(place.getName())
					.build();
			placeDtoList.add(placeDto);
		}
		
		return placeDtoList;
	}
	
	// 공연 장소 1개 조회
	@GetMapping("/{placeId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public PlaceDto getPlace(@PathVariable("placeId") Long placeId) {
		Place place = placeService.findPlaceById(placeId);
		PlaceDto placeDto = PlaceDto.builder()
				.name(place.getName())
				.build();
		return placeDto;
	}
	
	// 공연 장소 정보 수정
	@PutMapping("/{placeId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String modifyPlace(@PathVariable("placeId") Long placeId,
							  @Valid @RequestBody PlaceDto placeDto) {
		placeService.updatePlace(placeId, placeDto);
		return "장소 수정에 성공했습니다.";
	}
	
	// 공연 장소 삭제
	@DeleteMapping("/{placeId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String removePlace(@PathVariable("placeId") Long placeId) {
		placeService.deletePlace(placeId);
		return "장소(id: " + placeId + ") 삭제에 성공했습니다.";
	}
}
