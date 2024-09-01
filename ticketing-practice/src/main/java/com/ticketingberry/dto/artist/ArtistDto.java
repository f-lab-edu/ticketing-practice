package com.ticketingberry.dto.artist;

import com.ticketingberry.domain.artist.Artist;

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
public class ArtistDto {
	@NotNull(message = "아티스트 이름을 입력해주세요.")
	@Size(min = 1, max = 50, message = "아티스트 이름을 1 ~ 16자 사이로 입력해주세요.")
	private String name;
	
	public static ArtistDto of(Artist artist) {
		return ArtistDto.builder()
				.name(artist.getName())
				.build();
	}
	
//	public static Artist newArtist(ArtistDto artistDto) {
//		return Artist.builder()
//				.name(artistDto.getName())
//				.build();
//	}
}
