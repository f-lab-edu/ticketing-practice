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

import com.ticketingberry.domain.entity.Concert;
import com.ticketingberry.domain.entity.District;
import com.ticketingberry.dto.ConcertDto;
import com.ticketingberry.service.ConcertService;
import com.ticketingberry.service.DistrictService;
import com.ticketingberry.service.SeatService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ConcertController {
	private final ConcertService concertService;
	private final DistrictService districtService;
	private final SeatService seatService;
	
	// 공연 추가 (구역들과 좌석들도 한 번에 같이 추가)
	@PostMapping("/concerts")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ConcertDto addConcert(@Valid @RequestBody ConcertDto concertDto) {	
		// District 리스트의 값을 제외한 Concert를 우선 저장
		Concert concert =  concertService.create(concertDto);
		
		// District와 Seat을 차례대로 저장
		concertDto.getDistrictDtos().stream()
		.forEach(districtDto -> {
			// Seat 리스트의 값을 제외한 District를 우선 저장
			District district = districtService.create(districtDto, concert);
			// Seat을 District에 차례대로 저장
			districtDto.getSeatDtos().stream()
			.forEach(seatDto -> seatService.create(seatDto, district));
		});
		
		// District와 Seat이 포함된 Concert 엔티티를 DTO로 변환하여 응답 본문으로 반환
		return ConcertDto.of(concert);
	}
	
	// 전체 공연 목록 조회
	@GetMapping("/concerts")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConcertDto> getAllConcerts() {
		List<Concert> concertList = concertService.findAll();
		return entityListToDtoList(concertList);
	}
	
	// 공연 1개 조회
	@GetMapping("/concerts/{concertId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConcertDto getConcert(@PathVariable("concertId") Long concertId) {
		Concert concert = concertService.findById(concertId);
		return ConcertDto.of(concert);
	}
	
	// 1개의 장소에 해당하는 공연 리스트 조회
	@GetMapping("/places/{placeId}/concerts")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConcertDto> getConcertsByPlaceId(@PathVariable("placeId") Long placeId) {
		List<Concert> concertList = concertService.findListByPlaceId(placeId);
		return entityListToDtoList(concertList);
	}
	
	// 1팀의 아티스트에 해당하는 공연 리스트 조회
	@GetMapping("/artists/{artistId}/concerts")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConcertDto> getConcertsByArtistId(@PathVariable("artistId") Long artistId) {
		List<Concert> concertList = concertService.findListByArtistId(artistId);
		return entityListToDtoList(concertList);
	}
	
	// 공연 수정
	@PutMapping("/concerts/{concertId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConcertDto modifyConcert(@PathVariable("concertId") Long concertId,
								@Valid @RequestBody ConcertDto concertDto) {
		Concert concert = concertService.update(concertId, concertDto);
		return ConcertDto.of(concert);
	}
	
	// 공연 삭제
	@DeleteMapping("/concerts/{concertId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConcertDto removeConcert(@PathVariable("concertId") Long concertId) {
		Concert concert = concertService.delete(concertId);
		return ConcertDto.of(concert);
	}
	
	private List<ConcertDto> entityListToDtoList(List<Concert> concertList) {
		return concertList.stream()
				.map(concert -> ConcertDto.of(concert))
				.collect(Collectors.toList());
	}
}
