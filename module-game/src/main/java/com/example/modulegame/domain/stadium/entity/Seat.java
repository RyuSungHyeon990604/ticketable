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
public class Seat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, nullable = false)
	private String position;

	@Column(columnDefinition = "TINYINT(1)")
	private boolean isBlind;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "section_id", nullable = false)
	private Section section;

	private LocalDateTime deletedAt;

	@Builder
	public Seat(String position, boolean isBlind, Section section) {
		this.position = position;
		this.isBlind = isBlind;
		this.section = section;
		this.deletedAt = null;
	}

	public void updateBlind() {
		this.isBlind = !isBlind;
	}

	public void delete() {
		this.deletedAt = LocalDateTime.now();
	}
}
