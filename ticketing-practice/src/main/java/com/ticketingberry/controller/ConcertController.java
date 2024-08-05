package com.ticketingberry.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketingberry.domain.entity.Concert;
import com.ticketingberry.dto.ConcertDto;
import com.ticketingberry.dto.DistrictDto;
import com.ticketingberry.dto.SeatDto;
import com.ticketingberry.service.ConcertService;
import com.ticketingberry.service.DistrictService;
import com.ticketingberry.service.SeatService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concerts")
public class ConcertController {
	private final ConcertService concertService;
	private final DistrictService districtService;
	private final SeatService seatService;
	
	// 공연 추가 (구역들과 좌석들도 한 번에 같이 추가)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String addConcert(@Valid @RequestBody ConcertDto concertDto) {
		StringBuilder result = new StringBuilder();
		
		// District 리스트의 값을 제외한 Concert를 우선 저장
		Long concertId = concertService.createConcert(concertDto);
		result.append("공연(id: " + concertId + ")\n\n");
		
		List<DistrictDto> districtDtoList = concertDto.getDistricts();
		
		// District와 Seat 리스트 변환 및 저장
		for (DistrictDto districtDto : districtDtoList) {
			// 1개의 구역 생성
			Long districtId = districtService.createDistrict(concertId, districtDto);
			result.append("\t구역(id: " + districtId + ")\n");
			
			List<SeatDto> seatDtoList = districtDto.getSeats();
			
			for (SeatDto seatDto : seatDtoList) {
				Long seatId = seatService.createSeat(districtId, seatDto);
				result.append("\t\t좌석(id: " + seatId + ")\n");
			}
			
			result.append("\n");
		}
		
		result.append("추가에 성공했습니다.");
		
		return result.toString();
	}
	
	// 전체 공연 목록 조회
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConcertDto> getAllConcerts() {
		List<Concert> concertList = concertService.findAllConcerts();
		List<ConcertDto> concertDtoList = new ArrayList<>();
		
		for (Concert concert : concertList) {
			ConcertDto concertDto = ConcertDto.builder()
					.placeId(concert.getPlace().getId())
					.artistId(concert.getArtist().getId())
					.title(concert.getTitle())
					.content(concert.getContent())
					.openedTicketAt(concert.getOpenedTicketAt())
					.performedAt(concert.getPerformedAt())
					.districtIds(concert.getDistricts().stream()
							.map(district -> district.getId())
							.collect(Collectors.toList()))
					.build();
			
			concertDtoList.add(concertDto);
		}
		
		return concertDtoList;
	}
	
	// 공연 1개 조회
	@GetMapping("/{concertId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConcertDto getConcert(@PathVariable("concertId") Long concertId) {
		Concert concert = concertService.findConcertByConcertId(concertId);
		
		ConcertDto concertDto = ConcertDto.builder()
				.placeId(concert.getPlace().getId())
				.artistId(concert.getArtist().getId())
				.title(concert.getTitle())
				.content(concert.getContent())
				.openedTicketAt(concert.getOpenedTicketAt())
				.performedAt(concert.getPerformedAt())
				.build();
		
		return concertDto;
	}
	
	// 공연 수정
	@PutMapping("/{concertId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String modifyConcert(@PathVariable("concertId") Long concertId,
								@Valid @RequestBody ConcertDto concertDto) {
		concertService.updateConcert(concertId, concertDto);
		return "공연 수정에 성공했습니다.";
	}
	
	// 공연 삭제
	@DeleteMapping("/{concertId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String removeConcert(@PathVariable("concertId") Long concertId) {
		concertService.deleteConcert(concertId);
		return "공연(id: " + concertId + ") 삭제에 성공했습니다.";
	}
}
