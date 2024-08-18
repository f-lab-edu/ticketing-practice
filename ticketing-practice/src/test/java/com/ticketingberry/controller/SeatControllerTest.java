package com.ticketingberry.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static java.time.Month.*;
import static org.hamcrest.Matchers.*;

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
import com.ticketingberry.domain.reservation.Reservation;
import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.dto.seat.OutSeatDto;
import com.ticketingberry.exception.ExceptionAdvice;
import com.ticketingberry.exception.custom.AlreadySelectedSeatException;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.service.SeatService;

@WebMvcTest({SeatController.class, ExceptionAdvice.class})
public class SeatControllerTest extends AbstractRestDocsTests {
	@MockBean
	private SeatService seatService;
	
	private Seat seat;
	
	private District district;
	
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
		
		Concert concert = Concert.builder()
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
		
		seat = Seat.builder()
				.id(1L)
				.district(district)
				.rowNum(1)
				.seatNum(1)
				.build();
	}
	
	@Test
	@DisplayName("1개의 구역에 해당하는 좌석 목록 조회")
	void getSeatsByDistrictId() throws Exception {
		List<Seat> seats = List.of(seat,
				Seat.builder()
				.id(2L)
				.district(district)
				.rowNum(1)
				.seatNum(2)
				.build());
		
		when(seatService.findListByDistrictId(district.getId())).thenReturn(seats);
	
		MvcResult result = mockMvc.perform(get("/districts/{districtId}/seats", district.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		List<OutSeatDto> outSeatDtos
			= objectMapper.readValue(responseBody, new TypeReference<List<OutSeatDto>>() {});
		
		assertNotNull(outSeatDtos);
		assertEquals(seats.get(1).getSeatNum(), outSeatDtos.get(1).getSeatNum());
	}
	
	@Test
	@DisplayName("1개의 좌석 조회(좌석 선택), 이미 선택된 좌석인지 체크")
	void getAndCheckSelectedSeat() throws Exception {
		when(seatService.findByIdAndCheckSelected(seat.getId())).thenReturn(seat);
		
		MvcResult result = mockMvc.perform(get("/seats/{seatId}", seat.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		OutSeatDto outSeatDto = objectMapper.readValue(responseBody, OutSeatDto.class);
		
		assertNotNull(outSeatDto);
		assertEquals(seat.getSeatNum(), outSeatDto.getSeatNum());
	}
	
	@Test
	@DisplayName("좌석 1개 조회: seatId로 좌석 조회가 안 된 경우 404(NOT_FOUND) 응답")
	void getAndCheckSelectedSeat_whenSeatIdDoesNotExist_throwsNotFound() throws Exception {
		when(seatService.findByIdAndCheckSelected(1000L))
		.thenThrow(new DataNotFoundException("좌석을 찾을 수 없습니다."));
		
		mockMvc.perform(get("/seats/{seatId}", 1000L))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("좌석을 찾을 수 없습니다.")))
				.andExpect(jsonPath("$.status", is(404)));
	}
	
	@Test
	@DisplayName("이미 선택된 좌석인지 체크: seat으로 좌석 조회가 된 경우 같은 자리에 또 예매하면 중복이므로 409(CONFLICT) 응답")
	void getAndCheckSelectedSeat_whenReservationFindFromSeat__throwsConflict() throws Exception {		
		Reservation reservation = Reservation.builder()
				.seat(seat)
				.build();
		
		String districtName = reservation.getSeat().getDistrict().getDistrictName();
		int rowNum = reservation.getSeat().getRowNum();
		int seatNum = reservation.getSeat().getSeatNum();
		
		when(seatService.findByIdAndCheckSelected(seat.getId()))
		.thenThrow(new AlreadySelectedSeatException(districtName + "구역 " + rowNum + "열 " + seatNum + "번 : 이미 선택된 좌석입니다."));
		
		mockMvc.perform(get("/seats/{seatId}", seat.getId()))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message", is(districtName + "구역 " + rowNum + "열 " + seatNum + "번 : 이미 선택된 좌석입니다.")))
				.andExpect(jsonPath("$.status", is(409)));
	}
}
