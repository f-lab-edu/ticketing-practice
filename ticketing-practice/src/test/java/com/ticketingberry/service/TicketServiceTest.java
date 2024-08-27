package com.ticketingberry.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.domain.seat.SeatRepository;
import com.ticketingberry.domain.ticket.Ticket;
import com.ticketingberry.domain.ticket.TicketRepository;
import com.ticketingberry.domain.user.User;
import com.ticketingberry.domain.user.UserRepository;
import com.ticketingberry.dto.ticket.TicketRequest;
import com.ticketingberry.exception.custom.DataNotFoundException;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
	@Mock
	private TicketRepository ticketRepository;
	
	@Mock
	private SeatRepository seatRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private TicketService ticketService;
	
	private Ticket ticket;
	
	private TicketRequest ticketRequest;
	
	private Seat seat;
	
	private User user;
	
	private Sort sort;
	
	@BeforeEach
	void setUp() {
		ticket = Ticket.builder()
				.id(1L)
				.seat(seat)
				.user(user)
				.deposited(false)
				.createdAt(LocalDateTime.now())
				.build();
		
		ticketRequest = TicketRequest.builder()
				.seatId(1L)
				.userId(1L)
				.deposited(false)
				.build();
		
		seat = Seat.builder()
				.id(1L)
				.build();
		
		user = User.builder()
				.id(1L)
				.username("testuser")
				.build();
		
		sort = Sort.by(Sort.Order.desc("createdAt"));
	}
	
	@Test
	@DisplayName("티켓 예매 성공")
	void createTicket_success() {
		when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
		
		Ticket result = ticketService.create(ticketRequest);
		assertEquals(ticket, result);
	}
	
	@Test
	@DisplayName("티켓 예매: seatId로 좌석 조회가 안 된 경우 DataNotFoundException")
	void createTicket_whenUserIdDoesNotExist_throwsDataNotFoundException() {
		when(seatRepository.findById(seat.getId())).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> ticketService.create(ticketRequest));
		assertEquals("좌석을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("전체 티켓 목록 조회 성공")
	void findAllTickets_success() {
		List<Ticket> tickets = List.of(ticket, Ticket.builder().build());
		when(ticketRepository.findAll(sort)).thenReturn(tickets);
		List<Ticket> result = ticketService.findAll();
		assertEquals(tickets, result);
	}
	
	@Test
	@DisplayName("1명의 회원에 해당하는 티켓 목록 조회")
	void findTicketsByUserId_success() {
		List<Ticket> tickets = List.of(ticket, Ticket.builder().user(user).build());
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(ticketRepository.findByUser(user, sort)).thenReturn(tickets);
		
		List<Ticket> result = ticketService.findListByUserId(user.getId());
		assertEquals(tickets, result);
	}
	
	@Test
	@DisplayName("1명의 회원에 해당하는 티켓 목록 조회: userId로 좌석 조회가 안 된 경우 DataNotFoundException")
	void findTicketsByUserId_whenUserIdDoesNotExist_throwsDataNotFoundException() {
		when(userRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> ticketService.findListByUserId(2L));
		assertEquals("회원을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("티켓 1개 조회 성공")
	void findTicketById_success() {
		when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
		Ticket result = ticketService.findById(ticket.getId());
		assertEquals(ticket, result);
	}
	
	@Test
	@DisplayName("티켓 1개 조회: ticketId로 티켓 조회가 안 된 경우 DataNotFoundException")
	void findTicketById_whenTicketIdDoesNotExist_throwsDataNotFoundException() {
		when(ticketRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> ticketService.findById(2L));
		assertEquals("티켓을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("티켓 수정 성공")
	void updateTicket_success() {
		when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
		
		TicketRequest ticketUpdateRequest = TicketRequest.builder()
				.seatId(seat.getId())
				.userId(user.getId())
				.deposited(true)
				.build();
		
		when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
		
		Ticket result = ticketService.update(ticket.getId(), ticketUpdateRequest);
		assertEquals(true, result.isDeposited());
	}
	
	@Test
	@DisplayName("티켓 예매 취소 성공")
	void deleteTicket_success() {
		when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
		Ticket result = ticketService.delete(ticket.getId());
		verify(ticketRepository, times(1)).delete(ticket);
		assertEquals(ticket, result);
	}
}
