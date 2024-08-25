package com.ticketingberry.domain.place;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Place {	// 장소 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "place_id")
	private long id;	// 장소 고유 id (1부터 자동 증가)
	
	@NotNull
	@Column(length = 50)
	private String name;	// 장소 이름
	
	@CreationTimestamp
	private LocalDateTime createdAt;	// 장소 객체 생성 시간

	@LastModifiedDate
	private LocalDateTime updatedAt;	// 장소 객체 수정 시간
	
	public void update(String name) {
		this.name = name;
	}
}
