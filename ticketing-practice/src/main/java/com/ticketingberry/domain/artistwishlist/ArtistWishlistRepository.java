package com.ticketingberry.domain.artistwishlist;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.artist.Artist;
import com.ticketingberry.domain.user.User;

public interface ArtistWishlistRepository extends JpaRepository<ArtistWishlist, Long> {
	List<ArtistWishlist> findByUser(User user);		// 한 명의 회원이 소유한 아티스트 찜 리스트 찾기
	List<ArtistWishlist> findByArtist(Artist artist);	// 한 팀의 아티스트를 찜해놓은 아티스트 찜 리스트 찾기
}
