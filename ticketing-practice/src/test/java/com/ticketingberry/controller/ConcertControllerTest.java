package com.ticketingberry.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.*;
import static java.time.Month.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ticketingberry.controller.common.AbstractRestDocsTests;
import com.ticketingberry.domain.artist.Artist;
import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.domain.district.District;
import com.ticketingberry.domain.place.Place;
import com.ticketingberry.domain.seat.Seat;
import com.ticketingberry.dto.concert.InConcertDto;
import com.ticketingberry.dto.concert.OutConcertDto;
import com.ticketingberry.dto.district.InDistrictDto;
import com.ticketingberry.dto.seat.SeatDto;
import com.ticketingberry.exception.ExceptionAdvice;
import com.ticketingberry.exception.custom.DataNotFoundException;
import com.ticketingberry.exception.custom.InvalidDateException;
import com.ticketingberry.service.ConcertService;
import com.ticketingberry.service.DistrictService;
import com.ticketingberry.service.SeatService;

@WebMvcTest({ConcertController.class, ExceptionAdvice.class})
public class ConcertControllerTest extends AbstractRestDocsTests {
	@MockBean
	private ConcertService concertService;
	
	@MockBean
	private DistrictService districtService;
	
	@MockBean
	private SeatService seatService;
	
	private Concert concert;
	
	private InConcertDto inConcertDto;
	
	private Concert concertNoDistricts;
	
	private Place place;
	
	private Artist artist;
	
	@BeforeEach
	void setUp() {
		place = Place.builder()
				.id(1L)
				.name("서울월드컵경기장")
				.build();
		
		artist = Artist.builder()
				.id(1L)
				.name("아이유")
				.build();
		
		concertNoDistricts = Concert.builder()
				.id(1L)
				.place(place)
				.artist(artist)
				.title("2024 IU HEREH WORLD TOUR CONCERT ENCORE: THE WINNING")
				.content("공연 날짜: 2024.09.21(토) ~ 2024.09.22(일)")
				.openedTicketAt(LocalDateTime.of(2024, AUGUST, 12, 20, 00))
				.performedAt(LocalDateTime.of(2024, SEPTEMBER, 21, 19, 00))
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
		
		List<SeatDto> inSeatDtos = List.of(
				SeatDto.builder()
				.rowNum(1)
				.seatNum(1)
				.build(),
				SeatDto.builder()
				.rowNum(1)
				.seatNum(2)
				.build());
		
		List<InDistrictDto> inDistrictDtos = List.of(
				InDistrictDto.builder()
				.districtName("A")
				.seatDtos(inSeatDtos)
				.build(),
				InDistrictDto.builder()
				.districtName("B")
				.seatDtos(inSeatDtos)
				.build());
		
		inConcertDto = InConcertDto.builder()
			    .placeId(place.getId())
			    .artistId(artist.getId())
			    .title("2024 IU HEREH WORLD TOUR CONCERT ENCORE: THE WINNING")
			    .content("공연 날짜: 2024.09.21(토) ~ 2024.09.22(일)")
			    .openedTicketAt(LocalDateTime.of(2024, AUGUST, 12, 20, 0))
			    .performedAt(LocalDateTime.of(2024, SEPTEMBER, 21, 19, 0))
			    .districtDtos(inDistrictDtos)
			    .build();
	}
	
	@Test
	@DisplayName("공연 추가 (구역들과 좌석들도 한 번에 같이 추가)")
	void addConcert() throws Exception {
		when(concertService.create(any(InConcertDto.class))).thenReturn(concertNoDistricts);
		
		AtomicInteger counter = new AtomicInteger(1);
		
		inConcertDto.getDistrictDtos().forEach(districtDto -> {
			when(districtService.create(any(InDistrictDto.class), any(Concert.class)))
			.thenAnswer(invocation -> {
		        int i = counter.getAndIncrement();
		        District district = District.builder()
		            .id(i)
		            .concert(concertNoDistricts)
		            .districtName(districtDto.getDistrictName())
		            .build();
		        return district;
		    });
		
	        districtDto.getSeatDtos().forEach(seatDto -> {
	        	District district = District.builder().id(counter.get()).build();
	        	Seat seat = Seat.of(seatDto, district);
	            when(seatService.create(any(SeatDto.class), any(District.class))).thenReturn(seat);
	        });
		});
		
		MvcResult result = mockMvc.perform(post("/concerts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(inConcertDto)))
				.andExpect(status().isCreated())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		OutConcertDto outConcertDto = objectMapper.readValue(responseBody, OutConcertDto.class);
		
		assertNotNull(outConcertDto);
		assertEquals(concertNoDistricts.getDistricts().get(0).getId(), 
				outConcertDto.getDistrictIds().get(0));
	}
	
	// 공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠르면 예외 던지게 하기
	@Test
	@DisplayName("공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠르면 400(BAD REQUEST) 응답")
	void addConcert_whenPerformedAtIsBeforeThanOpenedTicketDate_throwsBadRequest() throws Exception {
		InConcertDto invalidDateInConcertDto = InConcertDto.builder()
			    .placeId(place.getId())
			    .artistId(artist.getId())
			    .title(inConcertDto.getTitle())
			    .content(inConcertDto.getTitle())
			    .openedTicketAt(LocalDateTime.of(2025, AUGUST, 12, 20, 0))
			    .performedAt(LocalDateTime.of(2024, SEPTEMBER, 21, 19, 0))
			    .districtDtos(inConcertDto.getDistrictDtos())
			    .build();
		
		when(concertService.create(any(InConcertDto.class)))
		.thenThrow(new InvalidDateException("공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠릅니다."));
	
		mockMvc.perform(post("/concerts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidDateInConcertDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("공연이 시작하는 날짜가 공연 예매가 열리는 날짜보다 빠릅니다.")))
				.andExpect(jsonPath("$.status", is(400)));
	}
	
	@Test
	@DisplayName("전체 공연 목록 조회")
	void getAllConcerts() throws Exception {
		Place place = Place.builder()
				.name("인천아시아드 주경기장")
				.build();
		
		Artist artist = Artist.builder()
				.name("싸이")
				.build();
		
		List<Concert> concerts = List.of(concert,
				Concert.builder()
				.id(2L)
				.place(place)
			    .artist(artist)
			    .districts(List.of(District.builder().id(3L).build(),
						   		   District.builder().id(4L).build()))
			    .title("싸이 흠뻑쇼 SUMMER SWAG 2024 - 인천")
			    .content("공연 시간 정보: 2024.08.17(토) 오후 6시, 2024.08.28(일) 오후 6시")
			    .openedTicketAt(LocalDateTime.of(2024, JUNE, 10, 12, 0))
			    .performedAt(LocalDateTime.of(2024, AUGUST, 17, 18, 0))
			    .build());
		
		when(concertService.findAll()).thenReturn(concerts);
		
		MvcResult result = mockMvc.perform(get("/concerts")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		List<OutConcertDto> outConcertDtos
			= objectMapper.readValue(responseBody, new TypeReference<List<OutConcertDto>>() {});
		
		assertNotNull(outConcertDtos);
		assertEquals(concerts.get(1).getPlace().getName(),
				outConcertDtos.get(1).getPlaceDto().getName());
	}
	
	
	@Test
	@DisplayName("공연 1개 조회")
	void getConcert() throws Exception {
		when(concertService.findById(concert.getId())).thenReturn(concert);
		
		MvcResult result = mockMvc.perform(get("/concerts/{concertId}", concert.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		OutConcertDto outConcertDto = objectMapper.readValue(responseBody, OutConcertDto.class);
		
		assertNotNull(outConcertDto);
		assertEquals(concert.getPlace().getName(), outConcertDto.getPlaceDto().getName());
	}
	
	@Test
	@DisplayName("공연 1개 조회: concertId로 공연 조회가 안 된 경우 404(NOT_FOUND) 응답")
	void getConcert_whenConcertIdDoesNotExist_throwsNotFound() throws Exception {
		when(concertService.findById(1000L))
		.thenThrow(new DataNotFoundException("공연을 찾을 수 없습니다."));
		
		mockMvc.perform(get("/concerts/{concertId}", 1000L)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("공연을 찾을 수 없습니다.")))
				.andExpect(jsonPath("$.status", is(404)));
	}
	
	@Test
	@DisplayName("1개의 장소에 해당하는 공연 목록 조회")
	void getConcertsByPlaceId() throws Exception {
		Artist artist = Artist.builder()
				.name("Bruno Mars")
				.build();
		
		List<Concert> concerts = List.of(concert,
				Concert.builder()
				.id(2L)
				.place(place)
			    .artist(artist)
			    .districts(List.of(District.builder().id(3L).build(),
						   		   District.builder().id(4L).build()))
			    .title("현대카드 슈퍼콘서트 27 브루노 마스(Bruno Mars)")
			    .content("공연 시간 정보: 2023.06.17(토) 오후 8시, 2023.06.18(일) 오후 8시")
			    .openedTicketAt(LocalDateTime.of(2023, MAY, 10, 20, 0))
			    .performedAt(LocalDateTime.of(2023, JUNE, 17, 20, 0))
			    .build());
		
		when(concertService.findListByPlaceId(place.getId())).thenReturn(concerts);
		
		MvcResult result = mockMvc.perform(get("/places/{placeId}/concerts", place.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		List<OutConcertDto> outConcertDtos
			= objectMapper.readValue(responseBody, new TypeReference<List<OutConcertDto>>() {});
		
		assertNotNull(outConcertDtos);
		assertEquals(concerts.get(1).getArtist().getName(),
				outConcertDtos.get(1).getArtistDto().getName());
	}
	
	@Test
	@DisplayName("1개의 장소에 해당하는 공연 목록 조회: placeId로 장소 조회가 안 된 경우 404(NOT_FOUND) 응답")
	void getConcertsByPlaceId_whenPlaceIdDoesNotExist_throwsNotFound() throws Exception {
		when(concertService.findListByPlaceId(1000L))
		.thenThrow(new DataNotFoundException("장소를 찾을 수 없습니다."));
		
		mockMvc.perform(get("/places/{placeId}/concerts", 1000L)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("장소를 찾을 수 없습니다.")))
				.andExpect(jsonPath("$.status", is(404)));
	}
	
	@Test
	@DisplayName("1팀의 아티스트에 해당하는 공연 목록 조회")
	void getConcertsByArtistId() throws Exception {
		Place place = Place.builder()
				.name("KSPO DOME")
				.build();
		
		List<Concert> concerts = List.of(concert,
				Concert.builder()
				.id(2L)
				.place(place)
			    .artist(artist)
			    .districts(List.of(District.builder().id(3L).build(),
						   		   District.builder().id(4L).build()))
			    .title("2023 아이유 팬콘서트 'I+UN1VER5E'")
			    .content("공연 시간 정보: 2023.09.23(토) 오후 6시, 2023.09.24(일) 오후 5시")
			    .openedTicketAt(LocalDateTime.of(2023, AUGUST, 15, 20, 0))
			    .performedAt(LocalDateTime.of(2023, SEPTEMBER, 23, 18, 0))
			    .build());
		
		when(concertService.findListByArtistId(artist.getId())).thenReturn(concerts);
		
		MvcResult result = mockMvc.perform(get("/artists/{artistId}/concerts", artist.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		List<OutConcertDto> outConcertDtos
			= objectMapper.readValue(responseBody, new TypeReference<List<OutConcertDto>>() {});
		
		assertNotNull(outConcertDtos);
		assertEquals(concerts.get(1).getPlace().getName(),
				outConcertDtos.get(1).getPlaceDto().getName());
	}
	
	@Test
	@DisplayName("1팀의 아티스트에 해당하는 공연 목록 조회: artistId로 아티스트 조회가 안 된 경우 404(NOT_FOUND) 응답")
	void getConcertsByArtistId_whenArtistIdDoesNotExist_throwsNotFound() throws Exception {
		when(concertService.findListByArtistId(1000L))
		.thenThrow(new DataNotFoundException("아티스트를 찾을 수 없습니다."));
		
		mockMvc.perform(get("/artists/{artistId}/concerts", 1000L)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("아티스트를 찾을 수 없습니다.")))
				.andExpect(jsonPath("$.status", is(404)));
	}
	
	@Test
	@DisplayName("공연 수정")
	void modifyConcert() throws Exception {
		InConcertDto updateInConcertDto = InConcertDto.builder()
			    .placeId(place.getId())
			    .artistId(artist.getId())
			    .title(inConcertDto.getTitle())
			    .content("공연 한 달 연기\n공연 날짜: 2024.10.19(토) ~ 2024.10.20(일)")
			    .openedTicketAt(LocalDateTime.of(2024, AUGUST, 12, 20, 0))
			    .performedAt(LocalDateTime.of(2024, OCTOBER, 19, 19, 0))
			    .districtDtos(inConcertDto.getDistrictDtos())
			    .build();
		
		concert.update(updateInConcertDto);
		
		when(concertService.update(eq(concert.getId()), any(InConcertDto.class))).thenReturn(concert);
		
		MvcResult result = mockMvc.perform(put("/concerts/{concertId}", concert.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateInConcertDto)))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		OutConcertDto outConcertDto = objectMapper.readValue(responseBody, OutConcertDto.class);
		
		assertNotNull(outConcertDto);
		assertEquals(concert.getPerformedAt(), outConcertDto.getPerformedAt());
	}
	
	@Test
	@DisplayName("공연 삭제")
	void removeConcert() throws Exception {
		when(concertService.delete(concert.getId())).thenReturn(concert);
		
		MvcResult result = mockMvc.perform(delete("/concerts/{concertId}", concert.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = result.getResponse().getContentAsString();
		
		OutConcertDto outConcertDto = objectMapper.readValue(responseBody, OutConcertDto.class);
		
		assertNotNull(outConcertDto);
		assertEquals(concert.getPlace().getName(), outConcertDto.getPlaceDto().getName());
	}
}