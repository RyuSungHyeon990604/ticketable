package com.example.moduleticket.domain.ticket.entity;

import com.example.modulecommon.entity.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.json.JsonWriter.Member;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Builder
	public TicketPayment(Integer totalPoint, Ticket ticket, Member member) {
		this.totalPoint = totalPoint;
		this.ticket = ticket;
		this.member = member;
	}
	
}
