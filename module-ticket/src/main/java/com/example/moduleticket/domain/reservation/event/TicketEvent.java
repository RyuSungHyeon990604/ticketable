package com.example.moduleticket.domain.reservation.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketEvent {
    private Long gameId;
    private Long seatId;
}
