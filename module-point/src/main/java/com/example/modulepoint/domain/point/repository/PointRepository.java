package com.example.modulepoint.domain.point.repository;

import java.util.Optional;

import com.example.modulepoint.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointRepository extends JpaRepository<Point, Long> {

	@Query("select p from Point p where p.memberId = :memberId")
	Optional<Point> findByMemberId(@Param("memberId") Long memberId);
}
