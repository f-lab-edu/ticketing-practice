package com.ticketingberry.dto.concert;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class ConcertDto {	
	@NotNull(message = "제목을 입력해주세요.")
	@Size(min = 1, max = 150, message = "제목을 1 ~ 50자 사이로 입력해주세요.")
	private String title;
	
	@NotNull(message = "내용을 입력해주세요.")
	@Size(min = 1, max = 6000, message = "내용을 1 ~ 2000자 사이로 입력해주세요.")
	private String content;
	
	@NotNull(message = "공연 예매가 열리는 시간을 선택해주세요.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime openedTicketAt;
	
	@NotNull(message = "공연이 시작하는 시간을 선택해주세요.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime performedAt;
}
