package com.ticketingberry.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.*;
import static java.time.Month.*;
import static com.ticketingberry.domain.user.UserRole.*;

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
import com.ticketingberry.domain.reservation.Reservation;
import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.domain.user.User;
import com.ticketingberry.dto.reservation.InReservationDto;
import com.ticketingberry.dto.reservation.OutReservationDto;
import com.ticketingberry.exception.ExceptionAdvice;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.service.ReservationService;

@WebMvcTest({ReservationController.class, ExceptionAdvice.class})
public class ReservationControllerTest extends AbstractRestDocsTests {
	@MockBean
	private ReservationService reservationService;
	
	private List<Reservation> reservations;
	
	private InReservationDto inReservationDto;
	
	private List<User> users;
	
	private List<Seat> seats;
	
	private District district;
	
	@BeforeEach
	void setUp() {
		users = List.of(
					User.builder()
					.id(1L)
					.username("apple123")
					.password("apple123")
					.nickname("사과")
					.email("apple123@example.com")
					.phone("01020000101")
					.birth("20000101")
					.gender("F")
					.role(USER)
					.createdAt(LocalDateTime.now())
					.build(),
					User.builder()
					.id(2L)
					.username("orange123")
					.password("orange123")
					.nickname("오렌지")
					.email("orange123@example.com")
					.phone("01019990918")
					.birth("19990918")
					.gender("F")
					.role(USER)
					.createdAt(LocalDateTime.now())
					.build());
		
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
		
		seats = List.of(
					Seat.builder()
					.id(1L)
					.district(district)
					.rowNum(1)
					.seatNum(1)
					.build(),
					Seat.builder()
					.id(2L)
					.district(district)
					.rowNum(2)
					.seatNum(5)
					.build());
		
		reservations = List.of(
					Reservation.builder()
					.id(1L)
					.seat(seats.get(0))
					.user(users.get(0))
					.deposited(false)
					.createdAt(LocalDateTime.now())
					.build(),
					Reservation.builder()
					.id(2L)
					.seat(seats.get(1))
					.user(users.get(1))
					.deposited(true)
					.createdAt(LocalDateTime.now())
					.build());
		
		inReservationDto = InReservationDto.builder()
				.seatId(seats.get(0).getId())
				.userId(users.get(0).getId())
				.deposited(false)
				.build();
	}
	
	@Test
	@DisplayName("좌석 예매")
	void addReservation() throws Exception {
		Reservation reservation = reservations.get(0);
		when(reservationService.create(any(InReservationDto.class))).thenReturn(reservation);
		
		MvcResult result = mockMvc.perform(post("/reservations")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(inReservationDto)))
				.andExpect(status().isCreated())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		OutReservationDto outReservationDto = objectMapper.readValue(responseBody, OutReservationDto.class);
		
		assertNotNull(outReservationDto);
		assertEquals(reservation.getSeat().getSeatNum(), outReservationDto.getSeatDto().getSeatNum());
		assertEquals(reservation.getUser().getUsername(), outReservationDto.getUserDto().getUsername());
		assertEquals(reservation.getSeat().getDistrict().getConcert().getTitle(),
				outReservationDto.getSeatDto().getDistrictDto().getConcertDto().getTitle());
	}
	
	@Test
	@DisplayName("전체 예매 목록 조회")
	void getAllReservations() throws Exception {
		when(reservationService.findAll()).thenReturn(reservations);
		
		MvcResult result = mockMvc.perform(get("/reservations")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		List<OutReservationDto> outReservationDtos
			= objectMapper.readValue(responseBody, new TypeReference<List<OutReservationDto>>() {});
		
		assertNotNull(outReservationDtos);
		assertEquals(reservations.get(1).getSeat().getSeatNum(), 
				outReservationDtos.get(1).getSeatDto().getSeatNum());
		assertEquals(reservations.get(1).getUser().getUsername(), 
				outReservationDtos.get(1).getUserDto().getUsername());
		assertEquals(reservations.get(1).getSeat().getDistrict().getConcert().getTitle(),
				outReservationDtos.get(1).getSeatDto().getDistrictDto().getConcertDto().getTitle());
	}
	
	@Test
	@DisplayName("1명의 회원에 해당하는 예매 목록 조회")
	void getReservationsByUserId() throws Exception {
		List<Reservation> reservationList = List.of(reservations.get(0));
		when(reservationService.findListByUserId(users.get(0).getId())).thenReturn(reservationList);
		
		MvcResult result = mockMvc.perform(get("/users/{userId}/reservations", users.get(0).getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		List<OutReservationDto> outReservationDtos
			= objectMapper.readValue(responseBody, new TypeReference<List<OutReservationDto>>() {});
		
		assertNotNull(outReservationDtos);
		assertEquals(reservations.get(0).getSeat().getSeatNum(), 
				outReservationDtos.get(0).getSeatDto().getSeatNum());
		assertEquals(reservations.get(0).getUser().getUsername(), 
				outReservationDtos.get(0).getUserDto().getUsername());
		assertEquals(reservations.get(0).getSeat().getDistrict().getConcert().getTitle(),
				outReservationDtos.get(0).getSeatDto().getDistrictDto().getConcertDto().getTitle());
	}
	
	@Test
	@DisplayName("예매 1개 조회")
	void getReservation() throws Exception {
		Reservation reservation = reservations.get(0);
		when(reservationService.findById(reservation.getId())).thenReturn(reservation);
		
		MvcResult result = mockMvc.perform(get("/reservations/{reservationId}", reservation.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		OutReservationDto outReservationDto = objectMapper.readValue(responseBody, OutReservationDto.class);
		
		assertNotNull(outReservationDto);
		assertEquals(reservation.getSeat().getSeatNum(), outReservationDto.getSeatDto().getSeatNum());
		assertEquals(reservation.getUser().getUsername(), outReservationDto.getUserDto().getUsername());
		assertEquals(reservation.getSeat().getDistrict().getConcert().getTitle(),
				outReservationDto.getSeatDto().getDistrictDto().getConcertDto().getTitle());
	}
	
	@Test
	@DisplayName("예매 1개 조회: reservationId로 예매 조회가 안 된 경우 404(NOT_FOUND) 응답")
	void getReservationById_whenReservationIdDoesNotExist_throwsNotFound() throws Exception {
		when(reservationService.findById(1000L))
		.thenThrow(new DataNotFoundException("예매를 찾을 수 없습니다."));
		
		mockMvc.perform(get("/reservations/{reservationId}", 1000L))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.message", is("예매를 찾을 수 없습니다.")))
		.andExpect(jsonPath("$.status", is(404)));
	}
	
	@Test
	@DisplayName("예매 수정 (입금 완료)")
	void modifyReservation() throws Exception {
		Reservation reservation = reservations.get(0);
		
		inReservationDto = InReservationDto.builder()
				.seatId(seats.get(0).getId())
				.userId(users.get(0).getId())
				.deposited(true)
				.build();
		
		reservation.update(true);
		
		when(reservationService.update(eq(reservation.getId()), any(InReservationDto.class))).thenReturn(reservation);
		
		MvcResult result = mockMvc.perform(put("/reservations/{reservationId}", reservation.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(inReservationDto)))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		OutReservationDto outReservationDto = objectMapper.readValue(responseBody, OutReservationDto.class);
		
		assertNotNull(outReservationDto);
		assertEquals(reservation.isDeposited(), outReservationDto.isDeposited());
		assertEquals(reservation.getSeat().getDistrict().getConcert().getTitle(),
				outReservationDto.getSeatDto().getDistrictDto().getConcertDto().getTitle());
	}
	
	@Test
	@DisplayName("예매 취소")
	void removeReservation() throws Exception {
		Reservation reservation = reservations.get(0);
		when(reservationService.delete(reservation.getId())).thenReturn(reservation);
		
		MvcResult result = mockMvc.perform(delete("/reservations/{reservationId}", reservation.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		OutReservationDto outReservationDto = objectMapper.readValue(responseBody, OutReservationDto.class);
		
		assertNotNull(outReservationDto);
		assertEquals(reservation.getSeat().getSeatNum(), outReservationDto.getSeatDto().getSeatNum());
		assertEquals(reservation.getUser().getUsername(), outReservationDto.getUserDto().getUsername());
		assertEquals(reservation.getSeat().getDistrict().getConcert().getTitle(),
				outReservationDto.getSeatDto().getDistrictDto().getConcertDto().getTitle());
	}
}
