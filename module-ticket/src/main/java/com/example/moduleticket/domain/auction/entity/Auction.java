package com.example.moduleticket.domain.auction.entity;

import com.example.modulecommon.entity.Timestamped;
import com.example.moduleticket.domain.member.entity.Member;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "auction", indexes = {
	@Index(name = "idx_deleted_at", columnList = "deleted_at"),
	@Index(name = "idx_deleted_at_created_at", columnList = "deleted_at, created_at")
})
public class Auction extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer startPoint;

	private Integer bidPoint;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auction_ticket_info_id", nullable = false)
	private AuctionTicketInfo auctionTicketInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket ticket;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_id", nullable = false)
	private Member seller;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bidder_id")
	private Member bidder;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime deletedAt;

	@Builder
	public Auction(Integer startPoint, Integer bidPoint, AuctionTicketInfo auctionTicketInfo, Ticket ticket,
		Member seller, Member bidder) {
		this.startPoint = startPoint;
		this.bidPoint = bidPoint;
		this.auctionTicketInfo = auctionTicketInfo;
		this.ticket = ticket;
		this.seller = seller;
		this.bidder = bidder;
	}

	public void setDeletedAt() {
		if (this.deletedAt == null) {
			this.deletedAt = LocalDateTime.now();
		}
	}

	public void updateBid(Member bidder, Integer bidPoint) {
		this.bidder = bidder;
		this.bidPoint = bidPoint;
	}

	public boolean isBidPointChanged(Integer currentBidPoint) {
		return !this.bidPoint.equals(currentBidPoint);
	}

	public boolean isTimeOver() {
		return this.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now());
	}

	public boolean isSameSellerAndBidder(Member bidder) {
		return this.seller.equals(bidder);
	}

	public boolean hasBidder() {
		return this.bidder != null;
	}

	public boolean isNotOwner(Member requestMember) {
		return !this.seller.equals(requestMember);
	}

	public boolean isBidPointEnough(Integer bidPoint) {
		return this.bidPoint >= bidPoint;
	}

	public boolean isSameBidder(Member bidder) {
		return this.bidder.equals(bidder);
	}
}
