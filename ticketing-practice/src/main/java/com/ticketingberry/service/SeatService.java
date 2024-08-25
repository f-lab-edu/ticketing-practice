package com.ticketingberry.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticketingberry.domain.district.District;
import com.ticketingberry.domain.district.DistrictRepository;
import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.domain.seat.SeatRepository;
import com.ticketingberry.domain.ticket.TicketRepository;
import com.ticketingberry.exception.custom.AlreadySelectedSeatException;
import com.ticketingberry.exception.custom.DataNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {
	private final SeatRepository seatRepository;
	private final DistrictRepository districtRepository;
	private final TicketRepository ticketRepository;
	
	// 1개의 구역에 해당하는 좌석 리스트 조회
	@Transactional
	public List<Seat> findListByDistrictId(Long districtId) {
		District district = findDistrict(districtId);
		return seatRepository.findByDistrict(district);
	}
	
	// 1개의 좌석 조회(좌석 선택), 이미 선택된 좌석인지 체크
	@Transactional
	public Seat findByIdAndCheckSelected(Long seatId) {
		Seat seat = seatRepository.findById(seatId)
				.orElseThrow(() -> new DataNotFoundException("좌석을 찾을 수 없습니다."));
		
		// seat으로 ticket 조회가 된 경우 같은 자리에 또 예매하면 중복이므로 409(CONFLICT) 던지기
		ticketRepository.findBySeat(seat).ifPresent(duplicatedTicket -> {
			String districtName = duplicatedTicket.getSeat().getDistrict().getDistrictName();
			int rowNum = duplicatedTicket.getSeat().getRowNum();
			int seatNum = duplicatedTicket.getSeat().getSeatNum();
			
			throw new AlreadySelectedSeatException(
					districtName + "구역 " + rowNum + "열 " + seatNum + ": 이미 선택된 좌석입니다.");
		});
		
		return seat;
	}
	
	// districtId로 구역 조회
	@Transactional
	private District findDistrict(Long districtId) {
		return districtRepository.findById(districtId)
				.orElseThrow(() -> new DataNotFoundException("구역을 찾을 수 없습니다."));
	}
}
