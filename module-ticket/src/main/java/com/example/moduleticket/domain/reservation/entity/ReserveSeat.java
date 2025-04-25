package com.example.moduleticket.domain.reservation.entity;

import com.example.moduleticket.feign.dto.SeatDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReserveSeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id", nullable = false)
	private Reservation reservation;

	@Column(nullable = false)
	private Long seatId;

	@Column(nullable = false)
	private Long sectionId;

	private ReserveSeat(Long sectionId, Long seatId) {
		this.seatId = seatId;
		this.sectionId = sectionId;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public static List<ReserveSeat> from(Long sectionId, List<SeatDto> seatDtos) {
		return seatDtos.stream().map(seatDto->new ReserveSeat(sectionId, seatDto.getSeatId())).toList();
	}
}
