package com.ticketingberry.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ticketingberry.domain.place.Place;
import com.ticketingberry.domain.place.PlaceRepository;
import com.ticketingberry.dto.place.PlaceDto;
import com.ticketingberry.exception.custom.DataNotFoundException;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {
	@Mock
	private PlaceRepository placeRepository;
	
	@InjectMocks
	private PlaceService placeService;
	
	private Place place;
	
	private PlaceDto placeDto;
	
	@BeforeEach
	void setUp() {
		place = Place.builder()
				.id(1L)
				.name("KSPO DOME")
				.build();
		
		placeDto = PlaceDto.builder()
				.name("KSPO DOME")
				.build();
	}
	
	@Test
	@DisplayName("공연 장소 생성 성공")
	void createPlace_success() {
		when(placeRepository.save(any(Place.class))).thenReturn(place);
		Place result = placeService.create(placeDto);
		assertEquals(place, result);
	}
	
	@Test
	@DisplayName("전체 공연 장소 목록 조회 성공")
	void findAllPlaces_success() {
		List<Place> places = List.of(place, Place.builder().build());
		when(placeRepository.findAll()).thenReturn(places);
		List<Place> result = placeService.findAll();
		assertEquals(places, result);
	}
	
	@Test
	@DisplayName("공연 장소 1개 조회 성공")
	void findPlaceById_success() {
		when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));
		Place result = placeService.findById(place.getId());
		assertEquals(place, result);
	}
	
	@Test
	@DisplayName("공연 장소 1개 조회: placeId로 장소 조회가 안 된 경우 DataNotFoundException")
	void findPlaceById_whenPlaceIdDoesNotExist_throwsDataNotFoundException() {
		when(placeRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> placeService.findById(2L));
		assertEquals("장소를 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("공연 장소 수정 성공")
	void updatePlace_success() {
		when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));
		when(placeRepository.save(any(Place.class))).thenReturn(place);
		
		placeDto = PlaceDto.builder()
				.name("올림픽 체조경기장")
				.build();
		
		Place result = placeService.update(place.getId(), placeDto);
		assertEquals(result.getName(), placeDto.getName());
	}
	
	@Test
	@DisplayName("공연 장소 삭제 성공")
	void deletePlace_success() {
		when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));
		Place result = placeService.delete(place.getId());
		verify(placeRepository, times(1)).delete(place);
		assertEquals(place, result);
	}
}
