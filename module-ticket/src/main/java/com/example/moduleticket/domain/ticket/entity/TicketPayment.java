package com.example.moduleticket.domain.ticket.entity;

import com.example.moduleticket.global.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TicketPayment extends Timestamped {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer totalPoint;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket ticket;
	
	@Column(nullable = false)
	private Long memberId;

	@Builder
	public TicketPayment(Integer totalPoint, Ticket ticket, Long memberId) {
		this.totalPoint = totalPoint;
		this.ticket = ticket;
		this.memberId = memberId;
	}
	
}
