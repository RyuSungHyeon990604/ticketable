package com.example.moduleticket.domain.reservation.repository;

import com.example.moduleticket.domain.reservation.entity.Reservation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query("select r "
		+ "   from Reservation r "
		+ "   join fetch r.reserveSeats "
		+ "  where r.id = :reservationId "
		+ "    and r.memberId = :memberId ")
	Optional<Reservation> findByIdMemberId(Long reservationId , Long memberId);

	@Modifying
	@Query("update Reservation r"
		+ "    set r.state = 'EXPIRED_PAYMENT' "
		+ "  where r.state = 'WAITING_PAYMENT' "
		+ "    and r.createdAt < :expiredLimit ")
	int updateExpiredReservations(LocalDateTime expiredLimit);

	boolean existsByReserveSeats_SeatIdInAndState(List<Long> reserveSeatIds, String state);
}
