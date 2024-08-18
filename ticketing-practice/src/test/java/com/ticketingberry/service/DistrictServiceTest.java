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

import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.domain.concert.ConcertRepository;
import com.ticketingberry.domain.district.District;
import com.ticketingberry.domain.district.DistrictRepository;
import com.ticketingberry.dto.district.InDistrictDto;
import com.ticketingberry.exception.custom.DataNotFoundException;

@ExtendWith(MockitoExtension.class)
public class DistrictServiceTest {
	@Mock
	private DistrictRepository districtRepository;
	
	@Mock
	private ConcertRepository concertRepository;
	
	@InjectMocks
	private DistrictService districtService;
	
	private District district;
	
	private InDistrictDto inDistrictDto;
	
	private Concert concert;
	
	@BeforeEach
	void setUp() {
		district = District.builder()
				.id(1L)
				.concert(concert)
				.build();
		
		inDistrictDto = InDistrictDto.builder()
				.build();
		
		concert = Concert.builder()
				.id(1L)
				.build();
	}
	
	@Test
	@DisplayName("구역 생성 성공")
	void createDistrict_success() {
		when(districtRepository.save(any(District.class))).thenReturn(district);
		District result = districtService.create(inDistrictDto, concert);
		assertEquals(district, result);
	}
	
	@Test
	@DisplayName("1개의 콘서트에 해당하는 구역 목록 조회 성공")
	void findDistrictsByConcertId_success() {
		List<District> districts = List.of(district, District.builder().build());
		when(concertRepository.findById(concert.getId())).thenReturn(Optional.of(concert));
		when(districtRepository.findByConcert(concert)).thenReturn(districts);
		
		List<District> result = districtService.findListByConcertId(concert.getId());
		assertEquals(districts, result);
	}
	
	@Test
	@DisplayName("1개의 콘서트에 해당하는 구역 목록 조회: concertId로 공연 조회가 안 된 경우 DataNotFoundException")
	void findDistrictsByConcertId_whenConcertIdDoesNotExist_throwsDataNotFoundException() {
		when(concertRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> districtService.findListByConcertId(2L));
		assertEquals("공연을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("구역 1개 조회 성공")
	void findDistrictById_success() {
		when(districtRepository.findById(district.getId())).thenReturn(Optional.of(district));
		District result = districtService.findById(district.getId());
		assertEquals(district, result);
	}
	
	@Test
	@DisplayName("구역 1개 조회: districtId로 공연 조회가 안 된 경우 DataNotFoundException")
	void findDistrictById_whenDistrictIdDoesNotExist_throwsDataNotFoundException() {
		when(districtRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> districtService.findById(2L));
		assertEquals("구역을 찾을 수 없습니다.", exception.getMessage());
	}
}
