package com.example.modulepoint.domain.point.repository;


import com.example.modulepoint.domain.point.entity.PointPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointPaymentRepository extends JpaRepository<PointPayment, Long> {
}
