package com.ticketingberry.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.*;
import static java.time.Month.*;
import static com.ticketingberry.domain.user.Gender.F;
import static com.ticketingberry.domain.user.Gender.M;
import static com.ticketingberry.domain.user.UserRole.*;

import java.time.LocalDate;
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
import com.ticketingberry.domain.ticket.Ticket;
import com.ticketingberry.domain.user.User;
import com.ticketingberry.dto.ticket.TicketRequest;
import com.ticketingberry.dto.ticket.TicketResponse;
import com.ticketingberry.exception.ExceptionAdvice;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.service.TicketService;

@WebMvcTest({TicketController.class, ExceptionAdvice.class})
public class TicketControllerTest extends AbstractRestDocsTests {
	@MockBean
	private TicketService ticketService;
	
	private List<Ticket> tickets;
	
	private TicketRequest ticketRequest;
	
	private List<User> users;
	
	private List<Seat> seats;
	
	private District district;
	
	@BeforeEach
	void setUp() {
		users = List.of(
					User.builder()
					.id(1L)
					.username("agi1004")
					.password("password")
					.nickname("오렌지")
					.email("agi1009@naver.com")
					.phone("01087872963")
					.birthAt(LocalDate.of(1999, SEPTEMBER, 18))
					.gender(F)
					.role(USER)
					.createdAt(LocalDateTime.now())
					.build(),
					User.builder()
					.id(2L)
					.username("apple123")
					.password("apple123")
					.nickname("사과")
					.email("apple123@example.com")
					.phone("01098765432")
					.birthAt(LocalDate.of(2002, APRIL, 20))
					.gender(M)
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
				.createdAt(LocalDateTime.now())
				.build();
		
		district = District.builder()
				.id(1L)
				.concert(concert)
				.seats(List.of(Seat.builder().id(1L).build(),
							   Seat.builder().id(2L).build()))
				.districtName("A")
				.createdAt(LocalDateTime.now())
				.build();
		
		seats = List.of(
					Seat.builder()
					.id(1L)
					.district(district)
					.rowNum(1)
					.seatNum(1)
					.createdAt(LocalDateTime.now())
					.build(),
					Seat.builder()
					.id(2L)
					.district(district)
					.rowNum(2)
					.seatNum(5)
					.createdAt(LocalDateTime.now())
					.build());
		
		tickets = List.of(
					Ticket.builder()
					.id(1L)
					.seat(seats.get(0))
					.user(users.get(0))
					.deposited(false)
					.createdAt(LocalDateTime.now())
					.build(),
					Ticket.builder()
					.id(2L)
					.seat(seats.get(1))
					.user(users.get(1))
					.deposited(true)
					.createdAt(LocalDateTime.now())
					.build());
		
		ticketRequest = TicketRequest.builder()
				.seatId(seats.get(0).getId())
				.userId(users.get(0).getId())
				.deposited(false)
				.build();
	}
	
	@Test
	@DisplayName("티켓 예매")
	void addTicket() throws Exception {
		Ticket ticket = tickets.get(0);
		when(ticketService.create(any(TicketRequest.class))).thenReturn(ticket);
		
		MvcResult result = mockMvc.perform(post("/tickets")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ticketRequest)))
				.andExpect(status().isCreated())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		TicketResponse ticketResponse = objectMapper.readValue(responseBody, TicketResponse.class);
		
		assertNotNull(ticketResponse);
		assertEquals(ticket.getSeat().getSeatNum(), ticketResponse.getSeatResponse().getSeatNum());
		assertEquals(ticket.getUser().getUsername(), ticketResponse.getUserResponse().getUsername());
		assertEquals(ticket.getSeat().getDistrict().getConcert().getTitle(),
				ticketResponse.getSeatResponse().getDistrictDto().getConcertResponse().getTitle());
	}
	
	@Test
	@DisplayName("전체 티켓 목록 조회")
	void getAllTickets() throws Exception {
		when(ticketService.findAll()).thenReturn(tickets);
		
		MvcResult result = mockMvc.perform(get("/tickets")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		List<TicketResponse> ticketResponses
			= objectMapper.readValue(responseBody, new TypeReference<List<TicketResponse>>() {});
		
		assertNotNull(ticketResponses);
		assertEquals(tickets.get(1).getSeat().getSeatNum(), 
				ticketResponses.get(1).getSeatResponse().getSeatNum());
		assertEquals(tickets.get(1).getUser().getUsername(), 
				ticketResponses.get(1).getUserResponse().getUsername());
		assertEquals(tickets.get(1).getSeat().getDistrict().getConcert().getTitle(),
				ticketResponses.get(1).getSeatResponse().getDistrictDto().getConcertResponse().getTitle());
	}
	
	@Test
	@DisplayName("1명의 회원에 해당하는 티켓 목록 조회")
	void getTicketsByUserId() throws Exception {
		List<Ticket> ticketList = List.of(tickets.get(0));
		when(ticketService.findListByUserId(users.get(0).getId())).thenReturn(ticketList);
		
		MvcResult result = mockMvc.perform(get("/users/{userId}/tickets", users.get(0).getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		List<TicketResponse> ticketResponses
			= objectMapper.readValue(responseBody, new TypeReference<List<TicketResponse>>() {});
		
		assertNotNull(ticketResponses);
		assertEquals(tickets.get(0).getSeat().getSeatNum(), 
				ticketResponses.get(0).getSeatResponse().getSeatNum());
		assertEquals(tickets.get(0).getUser().getUsername(), 
				ticketResponses.get(0).getUserResponse().getUsername());
		assertEquals(tickets.get(0).getSeat().getDistrict().getConcert().getTitle(),
				ticketResponses.get(0).getSeatResponse().getDistrictDto().getConcertResponse().getTitle());
	}
	
	@Test
	@DisplayName("티켓 1개 조회")
	void getTicket() throws Exception {
		Ticket ticket = tickets.get(0);
		when(ticketService.findById(ticket.getId())).thenReturn(ticket);
		
		MvcResult result = mockMvc.perform(get("/tickets/{ticketId}", ticket.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		TicketResponse ticketResponse = objectMapper.readValue(responseBody, TicketResponse.class);
		
		assertNotNull(ticketResponse);
		assertEquals(ticket.getSeat().getSeatNum(), ticketResponse.getSeatResponse().getSeatNum());
		assertEquals(ticket.getUser().getUsername(), ticketResponse.getUserResponse().getUsername());
		assertEquals(ticket.getSeat().getDistrict().getConcert().getTitle(),
				ticketResponse.getSeatResponse().getDistrictDto().getConcertResponse().getTitle());
	}
	
	@Test
	@DisplayName("티켓 1개 조회: ticketId로 티켓 조회가 안 된 경우 404(NOT_FOUND) 응답")
	void getTicketById_whenTicketIdDoesNotExist_throwsNotFound() throws Exception {
		when(ticketService.findById(1000L))
		.thenThrow(new DataNotFoundException("예매를 찾을 수 없습니다."));
		
		mockMvc.perform(get("/tickets/{ticketId}", 1000L))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.message", is("예매를 찾을 수 없습니다.")))
		.andExpect(jsonPath("$.status", is(404)));
	}
	
	@Test
	@DisplayName("티켓 수정 (입금 완료)")
	void modifyTicket() throws Exception {
		Ticket ticket = tickets.get(0);
		
		TicketRequest ticketUpdateRequest = TicketRequest.builder()
				.seatId(seats.get(0).getId())
				.userId(users.get(0).getId())
				.deposited(true)
				.build();
		
		ticket.update(true);
		
		when(ticketService.update(eq(ticket.getId()), any(TicketRequest.class))).thenReturn(ticket);
		
		MvcResult result = mockMvc.perform(put("/tickets/{ticketId}", ticket.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ticketUpdateRequest)))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		TicketResponse ticketResponse = objectMapper.readValue(responseBody, TicketResponse.class);
		
		assertNotNull(ticketResponse);
		assertEquals(ticket.isDeposited(), ticketResponse.isDeposited());
		assertEquals(ticket.getSeat().getDistrict().getConcert().getTitle(),
				ticketResponse.getSeatResponse().getDistrictDto().getConcertResponse().getTitle());
	}
	
	@Test
	@DisplayName("티켓 예매 취소")
	void removeTicket() throws Exception {
		Ticket ticket = tickets.get(0);
		when(ticketService.delete(ticket.getId())).thenReturn(ticket);
		
		MvcResult result = mockMvc.perform(delete("/tickets/{ticketId}", ticket.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		TicketResponse ticketResponse = objectMapper.readValue(responseBody, TicketResponse.class);
		
		assertNotNull(ticketResponse);
		assertEquals(ticket.getSeat().getSeatNum(), ticketResponse.getSeatResponse().getSeatNum());
		assertEquals(ticket.getUser().getUsername(), ticketResponse.getUserResponse().getUsername());
		assertEquals(ticket.getSeat().getDistrict().getConcert().getTitle(),
				ticketResponse.getSeatResponse().getDistrictDto().getConcertResponse().getTitle());
	}
}
