package com.example.moduleticket.domain.ticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TicketSeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket ticket;

	@Column(nullable = false)
	private Long seatId;

	@Column(nullable = false)
	private Long gameId;

	@Builder
	public TicketSeat(Ticket ticket, Long seatId, Long gameId) {
		this.ticket = ticket;
		this.seatId = seatId;
		this.gameId = gameId;
	}

}
