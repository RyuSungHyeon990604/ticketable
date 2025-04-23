//package com.example.moduleticket.domain.auction.entity;
//
//import com.example.modulecommon.entity.Timestamped;
//import com.example.moduleticket.domain.member.entity.Member;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.EntityListeners;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Temporal;
//import jakarta.persistence.TemporalType;
//import java.time.LocalDateTime;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//@EntityListeners(AuditingEntityListener.class)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Entity
//public class AuctionHistory extends Timestamped {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	private Integer point;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "auction_id", nullable = false)
//	private Auction auction;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "bidder_id", nullable = false)
//	private Member bidder;
//
//	@CreatedDate
//	@Column(updatable = false)
//	@Temporal(TemporalType.TIMESTAMP)
//	private LocalDateTime createdAt;
//
//	@Builder
//	public AuctionHistory(Integer point, Auction auction, Member bidder) {
//		this.point = point;
//		this.auction = auction;
//		this.bidder = bidder;
//	}
//}
