package com.example.moduleticket.domain.ticket.service;

import java.time.DayOfWeek;
import org.springframework.stereotype.Component;

@Component
public class TicketPriceCalculator {

	private static final int WEEKEND_ADDITIONAL_CHARGE = 500;

	public int calculateTicketPrice(Game game, List<Seat> seats) {
		int ticketPrice = game.getPoint() * seats.size();

		DayOfWeek dayOfWeek = game.getStartTime().getDayOfWeek();
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			ticketPrice = ticketPrice + (WEEKEND_ADDITIONAL_CHARGE * seats.size());
		}

		for (Seat seat : seats) {
			ticketPrice += seat.getSection().getExtraCharge();

		}

		return ticketPrice;
	}
}
