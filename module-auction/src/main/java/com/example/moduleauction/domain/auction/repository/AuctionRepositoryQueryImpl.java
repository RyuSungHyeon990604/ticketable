package com.example.moduleauction.domain.auction.repository;

import static com.example.moduleauction.domain.auction.entity.QAuction.*;
import static com.example.moduleauction.domain.auction.entity.QAuctionTicketInfo.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.moduleauction.domain.auction.dto.request.AuctionSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class AuctionRepositoryQueryImpl implements AuctionRepositoryQuery {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Long> findByConditions(AuctionSearchCondition dto, Pageable pageable) {
		BooleanExpression homeEq = dto.getHome() != null ? auction.auctionTicketInfo.home.eq(dto.getHome()) : null;
		BooleanExpression awayEq = dto.getAway() != null ? auction.auctionTicketInfo.away.eq(dto.getAway()) : null;
		BooleanExpression startTimeBetween = dto.getStartTime() != null
			? auction.auctionTicketInfo.gameStartTime.between(
			dto.getStartTime().toLocalDate().atStartOfDay(),
			dto.getStartTime().toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1)
		)
			: null;
		BooleanExpression seatCountEq =
			dto.getSeatCount() != null ? auction.auctionTicketInfo.seatCount.eq(dto.getSeatCount()) : null;
		BooleanExpression isTogether = dto.getIsTogether()
			? auction.auctionTicketInfo.isTogether.isTrue()
			: auction.auctionTicketInfo.isTogether.isFalse();
		BooleanExpression deletedAtIsNull = auction.deletedAt.isNull();

		List<Long> results = jpaQueryFactory
			.select(auction.id)
			.from(auction)
			.join(auction.auctionTicketInfo, auctionTicketInfo).fetchJoin()
			.where(homeEq, awayEq, startTimeBetween, seatCountEq, isTogether, deletedAtIsNull)
			.offset(pageable.getPageNumber())
			.limit(pageable.getPageSize())
			.orderBy(auction.createdAt.asc())
			.fetch();

		Long total = jpaQueryFactory
			.select(auction.countDistinct())
			.from(auction)
			.where(homeEq, awayEq, startTimeBetween, seatCountEq, isTogether, deletedAtIsNull)
			.fetchOne();

		return new PageImpl<>(results, pageable, total != null ? total : 0L);
	}
}
