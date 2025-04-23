package com.example.modulegame.domain.game.repository;


import com.example.modulegame.domain.game.entity.Game;
import com.example.modulegame.domain.game.entity.QGame;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameRepositoryQueryImpl implements GameRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Game> findGames(String team, LocalDateTime start, LocalDateTime end) {
        QGame game = QGame.game;

        return jpaQueryFactory
                .selectFrom(game)
                .where(
                        game.startTime.gt(LocalDateTime.now()),
                        game.ticketingStartTime.loe(LocalDateTime.now()),
                        start == null ? null : game.startTime.goe(start),
                        end == null ? null : game.startTime.loe(end),
                        team == null ? null : game.home.eq(team).or(game.away.eq(team))
                )
                .fetch();
    }
}
