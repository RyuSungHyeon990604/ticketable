package com.example.moduleticket.domain.ticket.entity;

import com.example.moduleticket.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false)
	private Long gameId;

	private LocalDateTime deletedAt;

	@Builder
	public Ticket(Long memberId, Long gameId) {
		this.memberId = memberId;
		this.gameId = gameId;
	}

	public void cancel() {
		deletedAt = LocalDateTime.now();
	}

	public void changeOwner(Member targetMember) {
		this.member = targetMember;
	}

	public boolean isTimeOverToAuction() {
		return this.game.getStartTime().minusHours(24).isBefore(LocalDateTime.now());
	}

	public boolean isNotOwner(Member seller) {
		return !this.member.equals(seller);
	}
}
