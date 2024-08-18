package com.ticketingberry.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.ticketingberry.domain.place.Place;
import com.ticketingberry.dto.place.PlaceDto;
import com.ticketingberry.service.PlaceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/places")
public class PlaceController {
	private final PlaceService placeService;
	
	// 공연 장소 추가
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PlaceDto addPlace(@Valid @RequestBody PlaceDto placeDto) {
		Place place = placeService.create(placeDto);
		return PlaceDto.of(place);
	}
	
	// 전체 공연 장소 목록 조회
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<PlaceDto> getAllPlaces() {
		List<Place> placeList = placeService.findAll();
		return placeList.stream()
				.map(place -> PlaceDto.of(place))
				.collect(Collectors.toList());
	}
	
	// 공연 장소 1개 조회
	@GetMapping("/{placeId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public PlaceDto getPlace(@PathVariable("placeId") Long placeId) {
		Place place = placeService.findById(placeId);
		return PlaceDto.of(place);
	}
	
	// 공연 장소 정보 수정
	@PutMapping("/{placeId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public PlaceDto modifyPlace(@PathVariable("placeId") Long placeId,
							  @Valid @RequestBody PlaceDto placeDto) {
		Place place = placeService.update(placeId, placeDto);
		return PlaceDto.of(place);
	}
	
	// 공연 장소 삭제
	@DeleteMapping("/{placeId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public PlaceDto removePlace(@PathVariable("placeId") Long placeId) {
		Place place = placeService.delete(placeId);
		return PlaceDto.of(place);
	}
}
