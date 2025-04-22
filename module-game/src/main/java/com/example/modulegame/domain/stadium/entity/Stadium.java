package com.example.modulegame.domain.stadium.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Stadium {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50)
	private String name;

	@Column(length = 100)
	private String location;

	private Integer capacity;

	private String imagePath;

	private LocalDateTime deletedAt;


	
	@Builder
	public Stadium(String name, String location, Integer capacity, String imagePath) {
		this.name = name;
		this.location = location;
		this.capacity = capacity;
		this.imagePath = imagePath;
		this.deletedAt = null;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void delete() {
		this.deletedAt = LocalDateTime.now();
	}

	public void updateImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void updateCapacity(int sum) {
		this.capacity = capacity + sum;
	}
}
