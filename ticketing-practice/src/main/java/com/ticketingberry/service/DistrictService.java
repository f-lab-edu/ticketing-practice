package com.ticketingberry.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.domain.concert.ConcertRepository;
import com.ticketingberry.domain.district.District;
import com.ticketingberry.domain.district.DistrictRepository;
import com.ticketingberry.dto.district.InDistrictDto;
import com.ticketingberry.exception.custom.DataNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistrictService {
	private final DistrictRepository districtRepository;
	private final ConcertRepository concertRepository;
	
	// 구역 추가
	@Transactional
	public District create(InDistrictDto inDistrictDto, Concert concert) {
		District district = District.of(inDistrictDto, concert);
		return districtRepository.save(district);
	}
	
	// 1개의 콘서트에 해당하는 구역 리스트 조회
	@Transactional
	public List<District> findListByConcertId(Long concertId) {
		Concert concert = findConcert(concertId);
		return districtRepository.findByConcert(concert);
	}
	
	// 1개의 구역 조회
	@Transactional
	public District findById(Long districtId) {
		return districtRepository.findById(districtId)
				.orElseThrow(() -> new DataNotFoundException("구역을 찾을 수 없습니다."));
	}
	
	// concertId로 콘서트 조회
	@Transactional
	private Concert findConcert(Long concertId) {
		return concertRepository.findById(concertId)
				.orElseThrow(() -> new DataNotFoundException("공연을 찾을 수 없습니다."));
	}
}
