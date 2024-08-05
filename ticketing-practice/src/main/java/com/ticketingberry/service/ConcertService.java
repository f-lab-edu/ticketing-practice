package com.ticketingberry.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ticketingberry.domain.entity.Artist;
import com.ticketingberry.domain.entity.Concert;
import com.ticketingberry.domain.entity.Place;
import com.ticketingberry.domain.repository.ArtistRepository;
import com.ticketingberry.domain.repository.ConcertRepository;
import com.ticketingberry.domain.repository.PlaceRepository;
import com.ticketingberry.dto.ConcertDto;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.exception.custom.InvalidDateException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConcertService {
	private final ConcertRepository concertRepository;
	private final PlaceRepository placeRepository;
	private final ArtistRepository artistRepository;
	
	// 공연 추가
	@Transactional
	public Long createConcert(ConcertDto concertDto) {
		LocalDateTime openedTicketAt = concertDto.getOpenedTicketAt();
		LocalDateTime performedAt = concertDto.getPerformedAt();
		// 공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠르면 예외 던지게 하기
		checkValidDate(openedTicketAt, performedAt);
		
		Place place = placeRepository.findById(concertDto.getPlaceId())
				.orElseThrow(() -> new DataNotFoundException("장소를 찾을 수 없습니다."));
		
		Artist artist = artistRepository.findById(concertDto.getArtistId())
				.orElseThrow(() -> new DataNotFoundException("아티스트를 찾을 수 없습니다."));
		
		Concert concert = Concert.builder()
				.place(place)
				.artist(artist)
				.title(concertDto.getTitle())
				.content(concertDto.getContent())
				.openedTicketAt(openedTicketAt)
				.performedAt(performedAt)
				.build();
		
		Concert createdConcert = concertRepository.save(concert);
		return createdConcert.getId();
	}
	
	// 전체 공연 목록 조회
	@Transactional
	public List<Concert> findAllConcerts() {
		return concertRepository.findAll();
	}
	
	// 공연 1개 조회
	@Transactional
	public Concert findConcertByConcertId(Long concertId) {
		return concertRepository.findById(concertId)
				.orElseThrow(() -> new DataNotFoundException("공연을 찾을 수 없습니다."));
	}
	
	// 공연 수정
	@Transactional
	public void updateConcert(Long concertId, ConcertDto concertDto) {
		// 공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠르면 예외 던지게 하기 
		checkValidDate(concertDto.getOpenedTicketAt(), concertDto.getPerformedAt());
		
		Concert concert = findConcertByConcertId(concertId);
		concert.update(concertDto);
		concertRepository.save(concert);
	}
	
	// 공연 삭제
	@Transactional
	public void deleteConcert(Long concertId) {
		Concert concert = findConcertByConcertId(concertId);
		concertRepository.delete(concert);
	}
	
	// 공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠르면 예외 던지게 하기
	private void checkValidDate(LocalDateTime openedTicketAt, LocalDateTime performedAt) {		
		if (performedAt.isBefore(openedTicketAt)) {
			throw new InvalidDateException("공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠릅니다.");
		}
	}
}
