package com.ticketingberry.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Component
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int id;
	
	@ManyToOne
	private User user;
	
	@OneToOne
	private Img img;
	
	@Column(length = 200, nullable = false)
	private String title;
	
	@Column(length = 5000, nullable = false)
	private String content;
	
	@Column(nullable = false)
	private int view = 0;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdDate;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime modifiedDate;
}
