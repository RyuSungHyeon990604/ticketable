package com.example.moduleticket.domain.point.entity;

import com.example.modulecommon.entity.Timestamped;
import com.example.moduleticket.domain.member.entity.Member;
import com.example.moduleticket.domain.point.enums.PointHistoryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PointHistory extends Timestamped {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer charge;
	
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private PointHistoryType type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;
	
	@Builder
	public PointHistory(Integer charge, PointHistoryType type, Member member) {
		this.charge = charge;
		this.type = type;
		this.member = member;
	}
	
	public void exchange() {
		this.type = PointHistoryType.EXCHANGE;
	}
}
