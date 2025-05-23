package com.example.moduleticket.domain.ticket.repository;

import com.example.moduleticket.domain.ticket.entity.TicketSeat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketSeatRepository extends JpaRepository<TicketSeat, Long> {

	@Query("select ts "
		+ "   from TicketSeat ts "
		+ "  where ts.ticket.id = :ticketId "
		+ "    and ts.ticket.deletedAt is null ")
	List<TicketSeat> findByTicketId(Long ticketId);

	boolean existsByGameIdAndSeatIdInAndTicketDeletedAtIsNull(Long gameId, List<Long> seatIds);
}
