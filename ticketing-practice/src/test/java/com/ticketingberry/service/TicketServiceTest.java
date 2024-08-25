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

import com.ticketingberry.domain.reservation.Reservation;
import com.ticketingberry.domain.reservation.ReservationRepository;
import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.domain.seat.SeatRepository;
import com.ticketingberry.domain.user.User;
import com.ticketingberry.domain.user.UserRepository;
import com.ticketingberry.dto.reservation.InReservationDto;
import com.ticketingberry.exception.custom.DataNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
	@Mock
	private ReservationRepository reservationRepository;
	
	@Mock
	private SeatRepository seatRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private ReservationService reservationService;
	
	private Reservation reservation;
	
	private InReservationDto reservationDto;
	
	private Seat seat;
	
	private User user;
	
	private Sort sort;
	
	@BeforeEach
	void setUp() {
		reservation = Reservation.builder()
				.id(1L)
				.seat(seat)
				.user(user)
				.deposited(false)
				.createdAt(LocalDateTime.now())
				.build();
		
		reservationDto = InReservationDto.builder()
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
	@DisplayName("좌석 예매 성공")
	void createReservation_success() {
		when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
		
		Reservation result = reservationService.create(reservationDto);
		assertEquals(reservation, result);
	}
	
	@Test
	@DisplayName("좌석 예매: seatId로 좌석 조회가 안 된 경우 DataNotFoundException")
	void createReservation_whenUserIdDoesNotExist_throwsDataNotFoundException() {
		when(seatRepository.findById(seat.getId())).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> reservationService.create(reservationDto));
		assertEquals("좌석을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("전체 예매 목록 조회 성공")
	void findAllReservations_success() {
		List<Reservation> reservations = List.of(reservation, Reservation.builder().build());
		when(reservationRepository.findAll(sort)).thenReturn(reservations);
		List<Reservation> result = reservationService.findAll();
		assertEquals(reservations, result);
	}
	
	@Test
	@DisplayName("1명의 회원에 해당하는 예매 목록 조회")
	void findReservationsByUserId_success() {
		List<Reservation> reservations = List.of(reservation, Reservation.builder().user(user).build());
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(reservationRepository.findByUser(user, sort)).thenReturn(reservations);
		
		List<Reservation> result = reservationService.findListByUserId(user.getId());
		assertEquals(reservations, result);
	}
	
	@Test
	@DisplayName("1명의 회원에 해당하는 예매 목록 조회: userId로 좌석 조회가 안 된 경우 DataNotFoundException")
	void findReservationsByUserId_whenUserIdDoesNotExist_throwsDataNotFoundException() {
		when(userRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> reservationService.findListByUserId(2L));
		assertEquals("회원을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("예매 1개 조회 성공")
	void findReservationById_success() {
		when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
		Reservation result = reservationService.findById(reservation.getId());
		assertEquals(reservation, result);
	}
	
	@Test
	@DisplayName("예매 1개 조회: reservationId로 좌석 조회가 안 된 경우 DataNotFoundException")
	void findReservationById_whenReservationIdDoesNotExist_throwsDataNotFoundException() {
		when(reservationRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> reservationService.findById(2L));
		assertEquals("예매를 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("예매 수정 성공")
	void updateReservation_success() {
		when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
		
		reservationDto = InReservationDto.builder()
				.seatId(1L)
				.userId(1L)
				.deposited(true)
				.build();
		
		when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
		
		Reservation result = reservationService.update(reservation.getId(), reservationDto);
		assertEquals(true, result.isDeposited());
	}
	
	@Test
	@DisplayName("예매 취소 성공")
	void deleteReservation_success() {
		when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
		Reservation result = reservationService.delete(reservation.getId());
		verify(reservationRepository, times(1)).delete(reservation);
		assertEquals(reservation, result);
	}
}
