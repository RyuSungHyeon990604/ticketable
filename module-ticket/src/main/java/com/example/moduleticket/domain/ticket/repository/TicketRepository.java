package com.example.moduleticket.domain.ticket.repository;

import com.example.moduleticket.domain.ticket.entity.Ticket;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	@Query("SELECT t "
		+ "   FROM Ticket t "
		+ "  WHERE t.id = :id "
		+ "    AND t.deletedAt is null "
		+ "    AND t.memberId = :memberId")
	Optional<Ticket> findByIdAndDeletedAtIsNull(Long id, Long memberId);

	@Query("SELECT t "
		+ "   FROM Ticket t  "
		+ "  WHERE t.memberId = :memberId "
		+ "    AND t.deletedAt is null ")
	List<Ticket> findAllByMemberIdWithGame(Long memberId);

	@Query("select t "
		+ "   from Ticket t "
		+ "   join fetch t.reservation "
		+ "  where t.reservation.id = :reservationId "
		+ "    and t.deletedAt is null ")
	Optional<Ticket> findByReservationId(Long reservationId);

	@Modifying
	@Query("update Ticket t "
		+ "    set t.deletedAt = now()"
		+ " where t.gameId = :gameId ")
	void softDeleteAllByGameId(Long gameId);
}
