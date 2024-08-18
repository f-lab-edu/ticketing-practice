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

import com.ticketingberry.domain.district.District;
import com.ticketingberry.domain.district.DistrictRepository;
import com.ticketingberry.domain.reservation.Reservation;
import com.ticketingberry.domain.reservation.ReservationRepository;
import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.domain.seat.SeatRepository;
import com.ticketingberry.dto.seat.SeatDto;
import com.ticketingberry.exception.custom.AlreadySelectedSeatException;
import com.ticketingberry.exception.custom.DataNotFoundException;

@ExtendWith(MockitoExtension.class)
public class SeatServiceTest {
	@Mock
	private SeatRepository seatRepository;
	
	@Mock
	private DistrictRepository districtRepository;
	
	@Mock
	private ReservationRepository reservationRepository;
	
	@InjectMocks
	private SeatService seatService;
	
	private Seat seat;
	
	private SeatDto inSeatDto;
	
	private District district;
	
	@BeforeEach
	void setUp() {
		district = District.builder()
				.id(1L)
				.districtName("A")
				.build();
		
		seat = Seat.builder()
				.id(1L)
				.district(district)
				.rowNum(1)
				.seatNum(1)
				.build();
		
		inSeatDto = SeatDto.builder()
				.build();
	}
	
	@Test
	@DisplayName("좌석 생성 성공")
	void createSeat_success() {
		when(seatRepository.save(any(Seat.class))).thenReturn(seat);
		Seat result = seatService.create(inSeatDto, district);
		assertEquals(seat, result);
	}
	
	@Test
	@DisplayName("1개의 구역에 해당하는 좌석 목록 조회 성공")
	void findSeatsByDistrictId_success() {
		List<Seat> seats = List.of(seat, Seat.builder().district(district).build());
		when(districtRepository.findById(district.getId())).thenReturn(Optional.of(district));
		when(seatRepository.findByDistrict(district)).thenReturn(seats);
		
		List<Seat> result = seatService.findListByDistrictId(district.getId());
		assertEquals(seats, result);
	}
	
	@Test
	@DisplayName("1개의 구역에 해당하는 좌석 목록 조회: districtId로 구역 조회가 안 된 경우 DataNotFoundException")
	void findSeatsByDistrictId_whenDistrictIdDoesNotExist_throwsDataNotFoundException() {
		when(districtRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> seatService.findListByDistrictId(2L));
		assertEquals("구역을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("좌석 1개 조회(좌석 선택), 이미 선택된 좌석인지 체크 성공")
	void findSeatByIdAndCheckNotSelected_success() {
		when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));
		when(reservationRepository.findBySeat(seat)).thenReturn(Optional.empty());
		Seat result = seatService.findByIdAndCheckSelected(seat.getId());
		assertEquals(seat, result);
	}
	
	@Test
	@DisplayName("좌석 1개 조회(좌석 선택): seatId로 좌석 조회가 안 된 경우 DataNotFoundException")
	void findSeatByIdAndCheckNotSelected_whenSeatIdDoesNotExist_throwsDataNotFoundException() {
		when(seatRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> seatService.findByIdAndCheckSelected(2L));
		assertEquals("좌석을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("이미 선택된 좌석인지 체크: seat으로 좌석 조회가 된 경우 같은 자리에 또 예매하면 중복이므로 AlreadySelectedSeatException")
	void findSeatByIdAndCheckSelected_whenReservationFindFromSeat__throwsDataNotFoundException() {
		when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));
		
		Reservation reservation = Reservation.builder()
				.seat(seat)
				.build();
		
		when(reservationRepository.findBySeat(seat)).thenReturn(Optional.of(reservation));
		
		AlreadySelectedSeatException exception
			= assertThrows(AlreadySelectedSeatException.class, () -> seatService.findByIdAndCheckSelected(seat.getId()));
		
		String districtName = reservation.getSeat().getDistrict().getDistrictName();
		int rowNum = reservation.getSeat().getRowNum();
		int seatNum = reservation.getSeat().getSeatNum();
		
		assertEquals(districtName + "구역 " + rowNum + "열 " + seatNum + "번 : 이미 선택된 좌석입니다.", exception.getMessage());
	}
}
