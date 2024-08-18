package com.ticketingberry.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ticketingberry.controller.common.AbstractRestDocsTests;
import com.ticketingberry.domain.place.Place;
import com.ticketingberry.dto.place.PlaceDto;
import com.ticketingberry.exception.ExceptionAdvice;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.service.PlaceService;

@WebMvcTest({PlaceController.class, ExceptionAdvice.class})
public class PlaceControllerTest extends AbstractRestDocsTests {
	@MockBean
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
	@DisplayName("공연 장소 추가")
	void addPlace() throws Exception {
		when(placeService.create(any(PlaceDto.class))).thenReturn(place);
		
		MvcResult result = mockMvc.perform(post("/places")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(placeDto)))
				.andExpect(status().isCreated())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		PlaceDto responsePlaceDto = objectMapper.readValue(responseBody, PlaceDto.class);
	
		assertNotNull(responsePlaceDto);
		assertEquals(placeDto.getName(), responsePlaceDto.getName());
	}
	
	@Test
	@DisplayName("전체 공연 장소 목록 조회")
	void getAllPlaces() throws Exception {
		List<Place> places = List.of(place, 
				Place.builder()
				.id(2L)
				.name("잠실체조경기장")
				.createdAt(LocalDateTime.now())
				.build());
		
		when(placeService.findAll()).thenReturn(places);
		
		MvcResult result = mockMvc.perform(get("/places")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		List<PlaceDto> responsePlaceDtos 
			= objectMapper.readValue(responseBody, new TypeReference<List<PlaceDto>>() {});
		
		assertNotNull(responsePlaceDtos);
		assertEquals(places.get(1).getName(), responsePlaceDtos.get(1).getName());
	}
	
	@Test
	@DisplayName("공연 장소 1개 조회")
	void getPlace() throws Exception {
		when(placeService.findById(place.getId())).thenReturn(place);
		
		MvcResult result = mockMvc.perform(get("/places/{placeId}", place.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		PlaceDto responsePlaceDto = objectMapper.readValue(responseBody, PlaceDto.class);
	
		assertNotNull(responsePlaceDto);
		assertEquals(placeDto.getName(), responsePlaceDto.getName());
	}
	
	@Test
	@DisplayName("공연 장소 1개 조회: placeId로 장소 조회가 안 된 경우 404(NOT_FOUND) 응답")
	void getPlace_whenPlaceIdDoesNotExist_throwsNotFound() throws Exception {
		when(placeService.findById(1000L))
		.thenThrow(new DataNotFoundException("장소를 찾을 수 없습니다."));
	
		mockMvc.perform(get("/places/{placeId}", 1000L)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("장소를 찾을 수 없습니다.")))
				.andExpect(jsonPath("$.status", is(404)));
	}
	
	@Test
	@DisplayName("공연 장소 수정")
	void modifyPlace() throws Exception {
		placeDto = PlaceDto.builder()
				.name("올림픽 체조경기장")
				.build();
		
		place.update("올림픽 체조경기장");
		
		when(placeService.update(eq(place.getId()), any(PlaceDto.class))).thenReturn(place);
		
		MvcResult result = mockMvc.perform(put("/places/{placeId}", place.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(placeDto)))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		PlaceDto responsePlaceDto = objectMapper.readValue(responseBody, PlaceDto.class);
		
		assertNotNull(responsePlaceDto);
		assertEquals(placeDto.getName(), responsePlaceDto.getName());
	}
	
	@Test
	@DisplayName("공연 장소 삭제")
	void removePlace() throws Exception {
		when(placeService.delete(place.getId())).thenReturn(place);
		
		MvcResult result = mockMvc.perform(delete("/places/{placeId}", place.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		PlaceDto responsePlaceDto = objectMapper.readValue(responseBody, PlaceDto.class);
		
		assertNotNull(responsePlaceDto);
		assertEquals(placeDto.getName(), responsePlaceDto.getName());
	}
}
