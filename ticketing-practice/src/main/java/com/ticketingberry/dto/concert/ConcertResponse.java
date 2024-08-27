package com.ticketingberry.dto.concert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ticketingberry.domain.concert.Concert;
import com.ticketingberry.dto.artist.ArtistDto;
import com.ticketingberry.dto.place.PlaceDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertResponse extends ConcertDto {
	private PlaceDto placeDto;
	
	private ArtistDto artistDto;
	
	@Builder.Default
	private List<Long> districtIds = new ArrayList<>();
	
	public static ConcertResponse of(Concert concert) {
		return ConcertResponse.builder()
				.placeDto(PlaceDto.of(concert.getPlace()))
				.artistDto(ArtistDto.of(concert.getArtist()))
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
