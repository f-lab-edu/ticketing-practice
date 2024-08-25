package com.ticketingberry.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ticketingberry.domain.artist.Artist;
import com.ticketingberry.domain.artist.ArtistRepository;
import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.domain.concert.ConcertRepository;
import com.ticketingberry.domain.district.District;
import com.ticketingberry.domain.place.Place;
import com.ticketingberry.domain.place.PlaceRepository;
import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.dto.concert.ConcertRequest;
import com.ticketingberry.dto.district.DistrictRequest;
import com.ticketingberry.dto.seat.SeatRequest;
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
	private final Sort sort = Sort.by(Sort.Order.desc("openedTicketAt"));
	
	// 공연 추가 (구역들과 좌석들도 한 번에 같이 추가)
	@Transactional
	public Concert create(ConcertRequest concertRequest) {
		// 공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠르면 예외 던지게 하기
		checkValidDate(concertRequest.getOpenedTicketAt(), concertRequest.getPerformedAt());
		
		Place place = findPlace(concertRequest.getPlaceId());
		Artist artist = findArtist(concertRequest.getArtistId());
		
		// District 리스트의 값을 제외한 Concert를 우선 생성
		Concert concert = ConcertRequest.newConcert(concertRequest, place, artist);
		
		// District와 Seat을 차례대로 저장
		concertRequest.getDistrictRequests().forEach(districtRequest -> {
			// Seat 리스트의 값을 제외한 District를 우선 생성
			District district = DistrictRequest.newDistrict(districtRequest, concert);
			concert.getDistricts().add(district);	// concert에 district 추가
			
			// Seat을 District에 차례대로 저장
			districtRequest.getSeatRequests().forEach(seatRequest -> {
				Seat seat = SeatRequest.newSeat(seatRequest, district);
				district.getSeats().add(seat);	// district에 seat 추가
			});
		});
		
		return concertRepository.save(concert);
	}
	
	// 전체 공연 목록 조회
	@Transactional
	public List<Concert> findAll() {
		return concertRepository.findAll(sort);
	}
	
	// 공연 1개 조회
	@Transactional
	public Concert findById(Long concertId) {
		return concertRepository.findById(concertId)
				.orElseThrow(() -> new DataNotFoundException("공연을 찾을 수 없습니다."));
	}
	
	// 1개의 장소에 해당하는 공연 리스트 조회
	@Transactional
	public List<Concert> findListByPlaceId(Long placeId) {
		Place place = findPlace(placeId);
		return concertRepository.findByPlace(place, sort);
	}
	
	// 1팀의 아티스트에 해당하는 공연 리스트 조회
	@Transactional
	public List<Concert> findListByArtistId(Long artistId) {
		Artist artist = findArtist(artistId);
		return concertRepository.findByArtist(artist, sort);
	}
	
	// 공연 수정
	@Transactional
	public Concert update(Long concertId, ConcertRequest concertRequest) {
		// 공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠르면 예외 던지게 하기 
		checkValidDate(concertRequest.getOpenedTicketAt(), concertRequest.getPerformedAt());
		Concert concert = findById(concertId);
		concert.update(concertRequest);
		return concertRepository.save(concert);
	}
	
	// 공연 삭제
	@Transactional
	public Concert delete(Long concertId) {
		Concert concert = findById(concertId);
		concertRepository.delete(concert);
		return concert;
	}
	
	// 공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠르면 예외 던지게 하기
	private void checkValidDate(LocalDateTime openedTicketAt, LocalDateTime performedAt) {		
		if (performedAt.isBefore(openedTicketAt)) {
			throw new InvalidDateException("공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠릅니다.");
		}
	}
	
	@Transactional
	private Place findPlace(Long placeId) {
		return placeRepository.findById(placeId)
				.orElseThrow(() -> new DataNotFoundException("장소를 찾을 수 없습니다."));
	}
	
	@Transactional
	private Artist findArtist(Long artistId) {
		return artistRepository.findById(artistId)
				.orElseThrow(() -> new DataNotFoundException("아티스트를 찾을 수 없습니다."));
	}
}
