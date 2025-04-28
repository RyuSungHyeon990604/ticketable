package com.example.modulepoint.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer price;
	
	@Column(nullable = false)
	private Long memberId;

	@Builder
	public PointPayment(Integer price, Long memberId) {
		this.price = price;
		this.memberId = memberId;
	}
}
