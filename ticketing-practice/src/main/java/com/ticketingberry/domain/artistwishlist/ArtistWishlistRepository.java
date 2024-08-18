package com.ticketingberry.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketingberry.domain.entity.Artist;
import com.ticketingberry.domain.entity.ArtistWishlist;
import com.ticketingberry.domain.entity.User;
import com.ticketingberry.domain.repository.common.UserRelatedRepository;

public interface ArtistWishlistRepository extends JpaRepository<ArtistWishlist, Long>,
												  UserRelatedRepository<ArtistWishlist> {
	List<ArtistWishlist> findByUser(User user);		// 한 명의 회원이 소유한 아티스트 찜 리스트 찾기
	List<ArtistWishlist> findByArtist(Artist artist);	// 한 팀의 아티스트를 찜해놓은 아티스트 찜 리스트 찾기
}
