package com.example.moduleticket.domain.reservation.repository;

import com.example.moduleticket.domain.reservation.entity.Reservation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query("select r "
		+ "   from Reservation r "
		+ "   join fetch r.reservations "
		+ "  where r.id = :id "
		+ "    and r.state = :state ")
	Optional<Reservation> findByIdAndStateWithSeatsAndGame(Long id, String state);
}
