package com.example.moduleticket.domain.point.repository;


import com.example.moduleticket.domain.point.entity.PointPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointPaymentRepository extends JpaRepository<PointPayment, Long> {
}
