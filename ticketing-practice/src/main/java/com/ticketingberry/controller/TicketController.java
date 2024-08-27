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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.domain.ticket.Ticket;
import com.ticketingberry.dto.ticket.TicketRequest;
import com.ticketingberry.dto.ticket.TicketResponse;
import com.ticketingberry.service.TicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TicketController {
	private final TicketService ticketService;
	
	// 티켓 예매
	@PostMapping("/tickets")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public TicketResponse addTicket(@Valid @RequestBody TicketRequest ticketRequest) {
		Ticket reservation = ticketService.create(ticketRequest);
		return TicketResponse.of(reservation);
	}
	
	// 전체 티켓 목록 조회
	@GetMapping("/tickets")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<TicketResponse> getAllTickets() {
		List<Ticket> ticketList = ticketService.findAll();
		return entityListToDtoList(ticketList);
	}
	
	// 1명의 회원에 해당하는 티켓 목록 조회
	@GetMapping("/users/{userId}/tickets")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<TicketResponse> getTicketsByUserId(@PathVariable("userId") Long userId) {
		List<Ticket> ticketList = ticketService.findListByUserId(userId);
		return entityListToDtoList(ticketList);
	}
	
	// 티켓 1개 조회
	@GetMapping("/tickets/{ticketId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TicketResponse getTicket(@PathVariable("ticketId") Long ticketId) {
		Ticket ticket = ticketService.findById(ticketId);
		return TicketResponse.of(ticket);
	}
	
	// 티켓 수정 (입금 완료)
	@PutMapping("/tickets/{ticketId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TicketResponse modifyTicket(@PathVariable("ticketId") Long ticketId,
									   @Valid @RequestBody TicketRequest ticketRequest) {
		Ticket ticket = ticketService.update(ticketId, ticketRequest);
		return TicketResponse.of(ticket);
	}
	
	// 티켓 예매 취소
	@DeleteMapping("/tickets/{ticketId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TicketResponse removeTicket(@PathVariable("ticketId") Long ticketId) {
		Ticket ticket = ticketService.delete(ticketId);
		return TicketResponse.of(ticket);
	}
	
	private List<TicketResponse> entityListToDtoList(List<Ticket> ticketList) {
		return ticketList.stream()
				.map(ticket -> TicketResponse.of(ticket))
				.collect(Collectors.toList());
	}
}
