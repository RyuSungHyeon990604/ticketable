package com.example.modulepoint.domain.payment.repository;


import com.example.modulepoint.domain.payment.entity.PointPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointPaymentRepository extends JpaRepository<PointPayment, Long> {
}
