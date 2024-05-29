package com.ticketingberry.model.entity;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Component
public class Img {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int id;
	
	@Column
	private String name;
	
	// 원본 이미지 데이터
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] data;
	
	// Base64로 압축된 이미지 데이터
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] compressedImageData;
}
