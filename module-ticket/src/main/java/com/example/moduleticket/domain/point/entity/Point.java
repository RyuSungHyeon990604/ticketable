package com.example.moduleticket.domain.point.entity;

import static com.example.modulecommon.exception.ErrorCode.NOT_ENOUGH_POINT;

import com.example.modulecommon.exception.ServerException;
import com.example.moduleticket.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Point {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer point;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;
	
	@Builder
	public Point(Integer point, Member member) {
		this.point = point;
		this.member = member;
	}
	
	public void plusPoint(Integer charge) {
		this.point += charge;
	}
	
	public void minusPoint(Integer charge) {
		if (this.point < charge) {
			throw new ServerException(NOT_ENOUGH_POINT);
		}
		this.point -= charge;
	}
}
