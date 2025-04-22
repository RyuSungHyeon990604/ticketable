package com.example.moduleticket.domain.ticket.repository;


import com.example.moduleticket.domain.ticket.entity.TicketPayment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketPaymentRepository extends JpaRepository<TicketPayment, Long> {
	Optional<TicketPayment> findByTicketId(Long ticketId);
}
