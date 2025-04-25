package com.example.modulegame.domain.stadium.repository;



import com.example.modulegame.domain.game.dto.SeatDto;
import java.util.List;

import com.example.modulegame.domain.stadium.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SeatRepository extends JpaRepository<Seat, Long> {

    boolean existsBySectionId(Long sectionId);

    @Query(
        " select new com.example.modulegame.domain.game.dto.SeatDto(" +
        " seat.id, " +
        " seat.position, " +
        " section.extraCharge, " +
        " section.extraCharge " +
        " ) " +
        "   from Game game " +
        "  inner join Stadium stadium " +
        "          on game.stadium = stadium " +
        "   left join Section section " +
        "          on section.stadium = stadium " +
        "         and section.id = :sectionId " +
        "   left join Seat seat " +
        "          on seat.section = section " +
        " where seat.id in :seatIds" +
        "   and game.id = :gameId "
    )
    List<SeatDto> findSeatDtosByGameAndSection(
        Long gameId,
        Long sectionId,
        List<Long> seatIds
    );

    @Query(
        " select new com.example.modulegame.domain.game.dto.SeatDto(" +
        " seat.id, " +
        " seat.position, " +
        " section.extraCharge, " +
        " section.extraCharge " +
        " ) " +
        "   from Game game " +
        "  inner join Stadium stadium " +
        "          on game.stadium = stadium " +
        "   left join Section section " +
        "          on section.stadium = stadium " +
        "   left join Seat seat " +
        "          on seat.section = section " +
        " where seat.id in :seatIds" +
        "   and game.id = :gameId "
    )
    List<SeatDto> findSeatDtosByGame(Long gameId, List<Long> seatIds);
}
