package com.ticketingberry.domain.image;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
public class Image {	// 사진 테이블
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private long id;	// 사진 고유 id (1부터 자동 증가)
	
	@Column
	private String name;	// 사진 이름
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] data;				// 원본 사진 데이터
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] compressedData;		// Base64로 압축한 사진 데이터
	
	@CreationTimestamp
	private LocalDateTime createdAt;	// 사진 객체 생성 시간
	
	@LastModifiedDate
	private LocalDateTime updatedAt;	// 사진 객체 수정 시간
}
