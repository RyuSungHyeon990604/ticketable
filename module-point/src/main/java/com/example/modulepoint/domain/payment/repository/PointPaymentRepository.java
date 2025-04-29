package com.example.modulepoint.domain.payment.repository;

import com.example.modulepoint.domain.payment.entity.PointPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointPaymentRepository extends JpaRepository<PointPayment, Long> {
	
	Optional<PointPayment> findByImpUid(String impUid);
}
