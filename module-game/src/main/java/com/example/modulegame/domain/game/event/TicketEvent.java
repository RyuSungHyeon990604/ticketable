package com.example.modulegame.domain.game.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketEvent {
    private Long gameId;
    private Long seatId;
}
