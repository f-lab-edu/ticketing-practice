package com.ticketingberry.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import com.ticketingberry.domain.entity.Concert;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertDto {
	@NotNull(message = "공연이 열리는 장소를 선택해주세요.")
	private Long placeId;
	
	@NotNull(message = "공연을 진행하는 아티스트를 선택해주세요.")
	private Long artistId;
	
	@NotNull(message = "공연에 속하는 구역들을 추가해주세요.")
	private List<@Valid DistrictDto> districtDtos;	// In
	
	@NotNull(message = "제목을 입력해주세요.")
	@Size(min = 1, max = 150, message = "제목을 1 ~ 50자 사이로 입력해주세요.")
	private String title;
	
	@NotNull(message = "내용을 입력해주세요.")
	@Size(min = 1, max = 6000, message = "내용을 1 ~ 2000자 사이로 입력해주세요.")
	private String content;
	
	@NotNull(message = "공연 예매가 열리는 시간을 선택해주세요.")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime openedTicketAt;
	
	@NotNull(message = "공연이 시작하는 시간을 선택해주세요.")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime performedAt;
	
	private List<Long> districtIds;		// Out
	
	public static ConcertDto of(Concert concert) {
		return ConcertDto.builder()
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
	}
}
