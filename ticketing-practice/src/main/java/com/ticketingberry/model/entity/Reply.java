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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Component
public class Reply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Concert concert;
	
	@ManyToOne
	private Board board;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdDate;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime modifiedDate;
}
