package com.ticketingberry.domain.artistwishlist;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import com.ticketingberry.domain.artist.Artist;
import com.ticketingberry.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ArtistWishlist {	// 아티스트 찜 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "artist_wishlist_id")
	private long id;		//  아티스트 찜 고유 id (1부터 자동 증가)
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "artist_id")
	private Artist artist;		// 찜을 해놓은 아티스트
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;			// 아티스트 찜을 소유하고 있는 회원
	
	@CreationTimestamp
	private LocalDateTime createdAt;	// 아티스트 찜 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 아티스트 찜 객체 수정 시간
}
