package com.example.moduleticket.domain.ticket.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GameDto {
	private Long id;
	private String stadium;
	private String away;
	private String home;
	private String type;
	private Integer point;
	private LocalDateTime startTime;
	private LocalDateTime ticketingStartTime;
	private LocalDateTime deletedAt;
}
