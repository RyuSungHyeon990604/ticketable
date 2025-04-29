package com.example.modulepoint.domain.exchange.repository;

import com.example.modulepoint.domain.exchange.entity.ExchangeHistory;
import com.example.modulepoint.domain.exchange.enums.ExchangeHistoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExchangeHistoryRepository extends JpaRepository<ExchangeHistory, Long> {
	
	boolean existsByMemberIdAndType(Long memberId, ExchangeHistoryType type);
	
	@Query("select eh from ExchangeHistory eh where eh.type = :type")
	Page<ExchangeHistory> findAllByType(@Param("type") ExchangeHistoryType type, Pageable pageable);
}
