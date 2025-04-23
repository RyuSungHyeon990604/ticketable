//package com.example.moduleticket.domain.auction.repository;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//@RequiredArgsConstructor
//public class AuctionRepositoryQueryImpl implements AuctionRepositoryQuery {
//
//	private final JPAQueryFactory jpaQueryFactory;
//
//	@Override
//	public Page<Auction> findByConditions(AuctionSearchCondition dto, Pageable pageable) {
//		BooleanExpression homeEq = dto.getHome() != null ? auction.ticket.game.home.eq(dto.getHome()) : null;
//		BooleanExpression awayEq = dto.getAway() != null ? auction.ticket.game.away.eq(dto.getAway()) : null;
//		BooleanExpression startTimeBetween = dto.getStartTime() != null
//			? auction.ticket.game.startTime.between(
//			dto.getStartTime().toLocalDate().atStartOfDay(),
//			dto.getStartTime().toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1)
//		)
//			: null;
//		BooleanExpression seatCountEq =
//			dto.getSeatCount() != null ? auction.auctionTicketInfo.seatCount.eq(dto.getSeatCount()) : null;
//		BooleanExpression isTogether = dto.getIsTogether()
//			? auction.auctionTicketInfo.isTogether.isTrue()
//			: auction.auctionTicketInfo.isTogether.isFalse();
//		BooleanExpression deletedAtIsNull = auction.deletedAt.isNull();
//
//		List<Auction> results = jpaQueryFactory
//			.selectFrom(auction)
//			.join(auction.ticket, ticket).fetchJoin()
//			.join(ticket.game, game).fetchJoin()
//			.join(auction.auctionTicketInfo, auctionTicketInfo).fetchJoin()
//			.join(auction.seller, member).fetchJoin()
//			.leftJoin(auction.bidder, member).fetchJoin()
//			.where(homeEq, awayEq, startTimeBetween, seatCountEq, isTogether, deletedAtIsNull)
//			.offset(pageable.getPageNumber())
//			.limit(pageable.getPageSize())
//			.orderBy(auction.createdAt.asc())
//			.fetch();
//
//		Long total = jpaQueryFactory
//			.select(auction.countDistinct())
//			.from(auction)
//			.where(homeEq, awayEq, startTimeBetween, seatCountEq, isTogether, deletedAtIsNull)
//			.fetchOne();
//
//		return new PageImpl<>(results, pageable, total != null ? total : 0L);
//	}
//}
