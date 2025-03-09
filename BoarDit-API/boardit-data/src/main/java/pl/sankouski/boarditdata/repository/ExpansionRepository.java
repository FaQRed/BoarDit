package pl.sankouski.boarditdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.sankouski.boarditdata.dto.ExpansionDTO;
import pl.sankouski.boarditdata.model.boardgame.Expansion;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpansionRepository extends JpaRepository<Expansion, Long> {
    Optional<Expansion> findByBggId(Long bggId);


    @Query("SELECT new pl.sankouski.boarditdata.dto.ExpansionDTO(e.id, e.name, e.description, e.yearPublished, e.minPlayers," +
            "e.maxPlayers, e.playingTime, e.imageLink) " +
            "FROM Expansion e JOIN e.boardGame b " +
            "WHERE b.id = :boardGameId")
    List<ExpansionDTO> getExpansionsByBoardGame_Id(@Param("boardGameId") Long boardGameId);
}
