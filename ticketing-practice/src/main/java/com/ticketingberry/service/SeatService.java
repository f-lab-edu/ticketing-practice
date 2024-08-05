package com.ticketingberry.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticketingberry.domain.entity.District;
import com.ticketingberry.domain.entity.Seat;
import com.ticketingberry.domain.repository.DistrictRepository;
import com.ticketingberry.domain.repository.SeatRepository;
import com.ticketingberry.dto.SeatDto;
import com.ticketingberry.exception.custom.DataNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {
	private final SeatRepository seatRepository;
	private final DistrictRepository districtRepository;
	
	// 좌석 추가
	@Transactional
	public Long createSeat(Long districtId, SeatDto seatDto) {
		District district = findDistrictByDistrictId(districtId);
		
		Seat seat = Seat.builder()
				.district(district)
				.rowNum(seatDto.getRowNum())
				.seatNum(seatDto.getSeatNum())
				.build();
		
		Seat createdSeat = seatRepository.save(seat);
		return createdSeat.getId();
	}
	
	// 1개의 구역에 해당하는 좌석 리스트 조회
	@Transactional
	public List<Seat> findSeatsByDistrictId(Long districtId) {
		District district = findDistrictByDistrictId(districtId);
		return seatRepository.findByDistrict(district);
	}
	
	// 1개의 좌석 조회
	@Transactional
	public Seat findSeatBySeatId(Long seatId) {
		return seatRepository.findById(seatId)
				.orElseThrow(() -> new DataNotFoundException("좌석을 찾을 수 없습니다."));
	}
	
	// districtId로 구역 조회
	@Transactional
	private District findDistrictByDistrictId(Long districtId) {
		return districtRepository.findById(districtId)
				.orElseThrow(() -> new DataNotFoundException("구역을 찾을 수 없습니다."));
	}
}
