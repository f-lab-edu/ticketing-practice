package com.ticketingberry.domain.concert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import com.ticketingberry.domain.artist.Artist;
import com.ticketingberry.domain.district.District;
import com.ticketingberry.domain.image.Image;
import com.ticketingberry.domain.place.Place;
import com.ticketingberry.dto.concert.ConcertRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
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
public class Concert {	// 콘서트(공연) 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "concert_id")
	private long id;		// 공연 고유 id (1부터 자동 증가)
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Place place;		// 공연이 열리는 장소
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Artist artist;	// 공연을 진행하는 아티스트
	
	// mappedBy = "concert": District 엔티티에서 이 관계의 소유자가 concert 필드임을 지정
	// ㄴ mappedBy 속성은 연관 관계의 주인을 정의하며, 주인은 외래 키가 있는 쪽을 가리킴
	// cascade = CascadeType.ALL: Concert 엔티티의 상태 변화가 자동으로 District 엔티티로 전파
	// ㄴ CascadeType.ALL은 모든 영속성 작업(저장, 삭제, 병합 등)이 전파
	// orphanRemoval = true: 부모 엔티티(Concert)와의 관계가 제거된 자식 엔티티(District)를 자동으로 삭제
	// ㄴ Concert에서 District가 제거되면 해당 District는 데이터베이스에서도 삭제
	@OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<District> districts = new ArrayList<>();	// 공연에 속하는 구역 리스트
	
	@OneToOne(fetch = FetchType.LAZY)
	private Image image;		// 공연 대표 이미지
	
	@NotNull
	@Column(length = 200)
	private String title;		// 공연 제목
	
	@NotNull
	@Column(length = 5000)
	private String content;		// 공연 내용
	
	@NotNull
	private long hits;		// 공연 조회수
	
	@NotNull
	private LocalDateTime openedTicketAt;	// 공연 예매가 열리는 시간
	
	@NotNull
	private LocalDateTime performedAt;		// 공연이 시작하는 시간	
	
	@CreationTimestamp
	private LocalDateTime createdAt;	// 공연 객체 생성 시간

	@LastModifiedDate
	private LocalDateTime updatedAt;	// 공연 객체 수정 시간
	
	public void update(ConcertRequest inConcertDto) {
		this.title = inConcertDto.getTitle();
		this.content = inConcertDto.getContent();
		this.openedTicketAt = inConcertDto.getOpenedTicketAt();
		this.performedAt = inConcertDto.getPerformedAt();
	}
}
