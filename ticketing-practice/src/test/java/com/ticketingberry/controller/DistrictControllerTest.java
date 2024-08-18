package com.ticketingberry.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static java.time.Month.*;
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
import com.ticketingberry.domain.artist.Artist;
import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.domain.district.District;
import com.ticketingberry.domain.place.Place;
import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.dto.district.OutDistrictDto;
import com.ticketingberry.exception.ExceptionAdvice;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.service.DistrictService;

@WebMvcTest({DistrictController.class, ExceptionAdvice.class})
public class DistrictControllerTest extends AbstractRestDocsTests {
	@MockBean
	private DistrictService districtService;
	
	private District district;
	
	private Concert concert;
	
	@BeforeEach
	void setUp() {
		Place place = Place.builder()
				.id(1L)
				.name("서울월드컵경기장")
				.build();
		
		Artist artist = Artist.builder()
				.id(1L)
				.name("아이유")
				.build();
		
		concert = Concert.builder()
				.id(1L)
				.place(place)
				.artist(artist)
				.districts(List.of(District.builder().id(1L).build(),
				   		   District.builder().id(2L).build()))
				.title("2024 IU HEREH WORLD TOUR CONCERT ENCORE: THE WINNING")
				.content("공연 날짜: 2024.09.21(토) ~ 2024.09.22(일)")
				.openedTicketAt(LocalDateTime.of(2024, AUGUST, 12, 20, 00))
				.performedAt(LocalDateTime.of(2024, SEPTEMBER, 21, 19, 00))
				.build();
		
		district = District.builder()
				.id(1L)
				.concert(concert)
				.seats(List.of(Seat.builder().id(1L).build(),
							   Seat.builder().id(2L).build()))
				.districtName("A")
				.build();
	}
	
	@Test
	@DisplayName("1개의 콘서트에 해당하는 구역 목록 조회")
	void getDistrictsByConcertId() throws Exception {
		List<District> districts = List.of(district,
				District.builder()
				.id(2L)
				.concert(concert)
				.seats(List.of(Seat.builder().id(3L).build(),
						   	   Seat.builder().id(4L).build()))
				.districtName("B")
				.build());
		
		when(districtService.findListByConcertId(concert.getId())).thenReturn(districts);
		
		MvcResult result = mockMvc.perform(get("/concerts/{concertId}/districts", concert.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		List<OutDistrictDto> outDistrictDtos
			= objectMapper.readValue(responseBody, new TypeReference<List<OutDistrictDto>>() {});
	
		assertNotNull(outDistrictDtos);
		assertEquals(districts.get(1).getDistrictName(), outDistrictDtos.get(1).getDistrictName());
	}
	
	@Test
	@DisplayName("구역 1개 조회")
	void getDisrict() throws Exception {
		when(districtService.findById(district.getId())).thenReturn(district);
		
		MvcResult result = mockMvc.perform(get("/districts/{districtId}", district.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		OutDistrictDto outDistrictDto = objectMapper.readValue(responseBody, OutDistrictDto.class);
		
		assertNotNull(outDistrictDto);
		assertEquals(district.getDistrictName(), outDistrictDto.getDistrictName());
	}
	
	@Test
	@DisplayName("구역 1개 조회: districtId로 구역 조회가 안 된 경우 404(NOT_FOUND) 응답")
	void getDistrict_whenDistrictIdDoesNotExist_throwsNotFound() throws Exception {
		when(districtService.findById(1000L))
		.thenThrow(new DataNotFoundException("구역을 찾을 수 없습니다."));
		
		mockMvc.perform(get("/districts/{districtId}", 1000L)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("구역을 찾을 수 없습니다.")))
				.andExpect(jsonPath("$.status", is(404)));
	}
}
