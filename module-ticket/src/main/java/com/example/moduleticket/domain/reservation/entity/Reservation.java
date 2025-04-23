package com.example.moduleticket.domain.reservation.entity;

import com.example.modulecommon.entity.Timestamped;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false)
	private Long gameId;

	//WAITING_PAYMENT, COMPLETE_PAYMENT, CANCEL_PAYMENT, EXPIRED_PAYMENT
	@Column(nullable = false)
	private String state;

	@Column(nullable = false)
	private int totalPrice;

	@OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ReserveSeat> reservations = new HashSet<>();

	public Reservation(Long memberId, Long gameId, String state, int totalPrice) {
		this.memberId = memberId;
		this.gameId = gameId;
		this.state = state;
		this.totalPrice = totalPrice;
	}

	public void addSeat(ReserveSeat reserveSeat) {
		reserveSeat.setReservation(this);
		this.reservations.add(reserveSeat);
	}

	public void completePayment() {
		this.state = "COMPLETE_PAYMENT";
	}

	public void cancelPayment() {
		this.state = "CANCEL_PAYMENT";
	}

	public void expiredPayment() {
		this.state = "EXPIRED_PAYMENT";
	}

}
