package com.example.moduleticket.domain.ticket.service;

import com.example.moduleticket.feign.dto.GameDto;
import com.example.moduleticket.feign.dto.SeatDto;
import java.time.DayOfWeek;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TicketPriceCalculator {

	private static final int WEEKEND_ADDITIONAL_CHARGE = 500;

	public int calculateTicketPrice(GameDto game, List<SeatDto> seats) {
		int ticketPrice = game.getPoint() * seats.size();

		DayOfWeek dayOfWeek = game.getStartTime().getDayOfWeek();
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			ticketPrice = ticketPrice + (WEEKEND_ADDITIONAL_CHARGE * seats.size());
		}

		for (SeatDto seat : seats) {
			ticketPrice += seat.getSectionExtraCharge();

		}

		return ticketPrice;
	}
}
