package com.example.modulegame.domain.game.dto;

import com.example.modulegame.domain.game.entity.Game;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameDto {
	private Long id;
	private String stadium;
	private String away;
	private String home;
	private String type;
	private Integer point;
	private LocalDateTime startTime;
	private LocalDateTime ticketingStartTime;
	private LocalDateTime deletedAt;

	public static GameDto from(Game game) {
		return new GameDto(
			game.getId(),
			game.getStadium().getName(),
			game.getAway(),
			game.getHome(),
			game.getType().name(),
			game.getPoint(),
			game.getStartTime(),
			game.getTicketingStartTime(),
			game.getDeletedAt()
		);
	}
}
