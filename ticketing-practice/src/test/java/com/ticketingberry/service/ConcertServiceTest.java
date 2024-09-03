package com.ticketingberry.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static java.time.Month.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.ticketingberry.domain.artist.Artist;
import com.ticketingberry.domain.artist.ArtistRepository;
import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.domain.concert.ConcertRepository;
import com.ticketingberry.domain.district.District;
import com.ticketingberry.domain.place.Place;
import com.ticketingberry.domain.place.PlaceRepository;
import com.ticketingberry.dto.concert.ConcertRequest;
import com.ticketingberry.dto.district.DistrictRequest;
import com.ticketingberry.dto.seat.SeatRequest;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.exception.custom.InvalidDateException;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceTest {
	@Mock
	private ConcertRepository concertRepository;
	
	@Mock
	private PlaceRepository placeRepository;
	
	@Mock
	private ArtistRepository artistRepository;
	
	@InjectMocks
	private ConcertService concertService;
	
	private Concert concert;
	
	private ConcertRequest concertRequest;
	
	private Place place;
	
	private Artist artist;
	
	private Sort sort;
	
	@BeforeEach
	void setUp() {
		place = Place.builder()
				.id(1L)
				.build();
		
		artist = Artist.builder()
				.id(1L)
				.build();
		
		concert = Concert.builder()
				.id(1L)
				.place(place)
				.artist(artist)
				.districts(List.of(District.builder().id(1L).build(),
				   		   District.builder().id(2L).build()))
				.title("2024 IU HEREH WORLD TOUR CONCERT ENCORE: THE WINNING")
				.content("공연 날짜: 2024.09.21(토) ~ 2024.09.22(일)")
				.openedTicketAt(LocalDateTime.of(2024, AUGUST, 12, 20, 00))
				.performedAt(LocalDateTime.of(2024, SEPTEMBER, 21, 19, 00))
				.build();
		
		List<SeatRequest> SeatRequests = List.of(
				SeatRequest.builder()
				.rowNum(1)
				.seatNum(1)
				.build(),
				SeatRequest.builder()
				.rowNum(1)
				.seatNum(2)
				.build());
		
		List<DistrictRequest> DistrictRequests = List.of(
				DistrictRequest.builder()
				.districtName("A")
				.seatRequests(SeatRequests)
				.build(),
				DistrictRequest.builder()
				.districtName("B")
				.seatRequests(SeatRequests)
				.build());
		
		concertRequest = ConcertRequest.builder()
			    .placeId(place.getId())
			    .artistId(artist.getId())
			    .title("2024 IU HEREH WORLD TOUR CONCERT ENCORE: THE WINNING")
			    .content("공연 날짜: 2024.09.21(토) ~ 2024.09.22(일)")
			    .openedTicketAt(LocalDateTime.of(2024, AUGUST, 12, 20, 0))
			    .performedAt(LocalDateTime.of(2024, SEPTEMBER, 21, 19, 0))
			    .districtRequests(DistrictRequests)
			    .build();

		sort = Sort.by(Sort.Order.desc("openedTicketAt"));
	}
	
	@Test
	@DisplayName("공연 생성 성공")
	void createConcert_success() {
		when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));
		when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
		when(concertRepository.save(any(Concert.class))).thenReturn(concert);
		
		Concert result = concertService.create(concertRequest);
		assertEquals(concert, result);
	}
	
	@Test
	@DisplayName("공연 생성 실패: 공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠르면 InvalidDateException")
	void createConcert_whenPerformedAtIsBeforeThanOpenedTicketDate_throwsInvalidDateException() {
		concertRequest = ConcertRequest.builder()
				.openedTicketAt(LocalDateTime.of(2024, FEBRUARY, 25, 17, 00))
				.performedAt(LocalDateTime.of(2024, FEBRUARY, 1, 20, 00))
				.build();
		
		InvalidDateException exception
			= assertThrows(InvalidDateException.class, () -> concertService.update(concert.getId(), concertRequest));
		assertEquals("공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠릅니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("전체 공연 목록 조회 성공")
	void findAllConcerts_success() {
		List<Concert> concerts = List.of(concert, Concert.builder().build());
		when(concertRepository.findAll(sort)).thenReturn(concerts);
		List<Concert> result = concertService.findAll();
		assertEquals(concerts, result);
	}
	
	@Test
	@DisplayName("공연 1개 조회 성공")
	void findConcertById_success() {
		when(concertRepository.findById(concert.getId())).thenReturn(Optional.of(concert));
		Concert result = concertService.findById(concert.getId());
		assertEquals(concert, result);
		assertEquals(concert.getHits(), result.getHits());
	}
	
	@Test
	@DisplayName("공연 1개 조회: concertId로 공연 조회가 안 된 경우 DataNotFoundException")
	void findConcertById_whenConcertIdDoesNotExist_throwsDataNotFoundException() {
		when(concertRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> concertService.findById(2L));
		assertEquals("공연을 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("1개의 장소에 해당하는 공연 목록 조회 성공")
	void findConcertsByPlaceId_success() {
		List<Concert> concerts = List.of(concert, Concert.builder().place(place).build());
		when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));
		
		
		when(concertRepository.findByPlace(place, sort)).thenReturn(concerts);
		
		List<Concert> result = concertService.findListByPlaceId(place.getId());
		assertEquals(concerts, result);
	}
	
	@Test
	@DisplayName("장소 1개 조회: placeId로 장소 조회가 안 된 경우 DataNotFoundException")
	void findConcertsByPlaceId_whenPlaceIdDoesNotExist_throwsDataNotFoundException() {
		when(placeRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> concertService.findListByPlaceId(2L));
		assertEquals("장소를 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("1팀의 아티스트에 해당하는 공연 목록 조회 성공")
	void findConcertsByArtistId_success() {
		List<Concert> concerts = List.of(concert, Concert.builder().artist(artist).build());
		when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
		when(concertRepository.findByArtist(artist, sort)).thenReturn(concerts);
		
		List<Concert> result = concertService.findListByArtistId(artist.getId());
		assertEquals(concerts, result);
	}
	
	@Test
	@DisplayName("아티스트 1팀 조회: artistId로 아티스트 조회가 안 된 경우 DataNotFoundException")
	void findConcertsByArtistId_whenArtistIdDoesNotExist_throwsDataNotFoundException() {
		when(artistRepository.findById(2L)).thenReturn(Optional.empty());
		DataNotFoundException exception
			= assertThrows(DataNotFoundException.class, () -> concertService.findListByArtistId(2L));
		assertEquals("아티스트를 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("공연 수정 성공")
	void updateConcert_success() {
		when(concertRepository.findById(concert.getId())).thenReturn(Optional.of(concert));
		when(concertRepository.save(any(Concert.class))).thenReturn(concert);
		
		concertRequest = ConcertRequest.builder()
				.placeId(1L)
				.artistId(1L)
				.openedTicketAt(LocalDateTime.of(2024, SEPTEMBER, 1, 20, 00))
				.performedAt(LocalDateTime.of(2024, SEPTEMBER, 18, 18, 00))
				.build();
		
		Concert result = concertService.update(concert.getId(), concertRequest);
		assertEquals(concertRequest.getPerformedAt(), result.getPerformedAt());
	}
	
	@Test
	@DisplayName("공연 삭제 성공")
	void deleteConcert_success() {
		when(concertRepository.findById(concert.getId())).thenReturn(Optional.of(concert));
		Concert result = concertService.delete(concert.getId());
		verify(concertRepository, times(1)).delete(concert);
		assertEquals(concert, result);
	}
}
