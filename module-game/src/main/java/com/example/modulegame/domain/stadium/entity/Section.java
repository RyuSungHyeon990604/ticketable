package com.example.modulegame.domain.stadium.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLRestriction("deleted_at is null")
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20)
	private String type;

	@Column(length = 20)
	private String code;

	private Integer extraCharge;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stadium_id", nullable = false)
	private Stadium stadium;

	private LocalDateTime deletedAt;
	
	@Builder
	public Section(String type, String code, Integer extraCharge, Stadium stadium) {
		this.type = type;
		this.code = code;
		this.extraCharge = extraCharge;
		this.stadium = stadium;
		this.deletedAt = null;
	}

	public void updateType(String type) {
		this.type = type;
	}

	public void updateCode(String code) {
		this.code = code;
	}

	public void updateExtraChange(Integer extraCharge) {
		this.extraCharge = extraCharge;
	}

	public void delete() {
		this.deletedAt = LocalDateTime.now();
	}
}
