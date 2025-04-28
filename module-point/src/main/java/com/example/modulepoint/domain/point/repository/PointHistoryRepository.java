package com.example.modulepoint.domain.point.repository;

import com.example.modulepoint.domain.point.entity.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

	@Query("select ph from PointHistory ph where ph.memberId = :memberId")
	Page<PointHistory> findAllByMemberId(Long memberId, Pageable pageable);
}
