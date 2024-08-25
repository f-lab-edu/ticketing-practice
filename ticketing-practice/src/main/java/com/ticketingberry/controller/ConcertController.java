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

import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.dto.concert.ConcertRequest;
import com.ticketingberry.dto.concert.ConcertResponse;
import com.ticketingberry.service.ConcertService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ConcertController {
	private final ConcertService concertService;
	
	// 공연 추가 (구역들과 좌석들도 한 번에 같이 추가)
	@PostMapping("/concerts")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ConcertResponse addConcert(@Valid @RequestBody ConcertRequest concertRequest) {	
		Concert concert = concertService.create(concertRequest);
		return ConcertResponse.of(concert);
	}
	
	// 전체 공연 목록 조회
	@GetMapping("/concerts")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConcertResponse> getAllConcerts() {
		List<Concert> concertList = concertService.findAll();
		return entityListToDtoList(concertList);
	}
	
	// 공연 1개 조회
	@GetMapping("/concerts/{concertId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConcertResponse getConcert(@PathVariable("concertId") Long concertId) {
		Concert concert = concertService.findById(concertId);
		return ConcertResponse.of(concert);
	}
	
	// 1개의 장소에 해당하는 공연 리스트 조회
	@GetMapping("/places/{placeId}/concerts")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConcertResponse> getConcertsByPlaceId(@PathVariable("placeId") Long placeId) {
		List<Concert> concertList = concertService.findListByPlaceId(placeId);
		return entityListToDtoList(concertList);
	}
	
	// 1팀의 아티스트에 해당하는 공연 리스트 조회
	@GetMapping("/artists/{artistId}/concerts")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConcertResponse> getConcertsByArtistId(@PathVariable("artistId") Long artistId) {
		List<Concert> concertList = concertService.findListByArtistId(artistId);
		return entityListToDtoList(concertList);
	}
	
	// 공연 수정
	@PutMapping("/concerts/{concertId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConcertResponse modifyConcert(@PathVariable("concertId") Long concertId,
								@Valid @RequestBody ConcertRequest concertRequest) {
		Concert concert = concertService.update(concertId, concertRequest);
		return ConcertResponse.of(concert);
	}
	
	// 공연 삭제
	@DeleteMapping("/concerts/{concertId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConcertResponse removeConcert(@PathVariable("concertId") Long concertId) {
		Concert concert = concertService.delete(concertId);
		return ConcertResponse.of(concert);
	}
	
	private List<ConcertResponse> entityListToDtoList(List<Concert> concertList) {
		return concertList.stream()
				.map(concert -> ConcertResponse.of(concert))
				.collect(Collectors.toList());
	}
}
