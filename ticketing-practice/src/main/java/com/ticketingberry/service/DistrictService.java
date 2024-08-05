package com.ticketingberry.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticketingberry.domain.entity.Concert;
import com.ticketingberry.domain.entity.District;
import com.ticketingberry.domain.repository.ConcertRepository;
import com.ticketingberry.domain.repository.DistrictRepository;
import com.ticketingberry.dto.DistrictDto;
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
	public Long createDistrict(Long concertId, DistrictDto districtDto) {
		Concert concert = findConcertByConcertId(concertId);
		
		District district = District.builder()
				.concert(concert)
				.districtName(districtDto.getDistrictName())
				.build();
		
		District createdDistrict = districtRepository.save(district);
		return createdDistrict.getId();
	}
	
	// 1개의 콘서트에 해당하는 구역 리스트 조회
	@Transactional
	public List<District> findDistrictsByConcertId(Long concertId) {
		Concert concert = findConcertByConcertId(concertId);
		return districtRepository.findByConcert(concert);
	}
	
	// 1개의 구역 조회
	@Transactional
	public District findDistrictByDistrictId(Long districtId) {
		return districtRepository.findById(districtId)
				.orElseThrow(() -> new DataNotFoundException("구역을 찾을 수 없습니다."));
	}
	
	// concertId로 콘서트 조회
	@Transactional
	private Concert findConcertByConcertId(Long concertId) {
		return concertRepository.findById(concertId)
				.orElseThrow(() -> new DataNotFoundException("공연을 찾을 수 없습니다."));
	}
}
