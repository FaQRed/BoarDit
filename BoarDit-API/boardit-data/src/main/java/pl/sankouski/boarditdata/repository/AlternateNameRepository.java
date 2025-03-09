package pl.sankouski.boarditdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.sankouski.boarditdata.dto.AlternateNameDTO;
import pl.sankouski.boarditdata.model.boardgame.AlternateName;
import pl.sankouski.boarditdata.model.boardgame.Artist;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlternateNameRepository extends JpaRepository<AlternateName, Long> {


    @Query("SELECT new pl.sankouski.boarditdata.dto.AlternateNameDTO(a.name, a.id) " +
            "FROM AlternateName a JOIN a.boardGame b " +
            "WHERE b.id = :boardGameId")
    List<AlternateNameDTO> getAlternateNamesByBoardGame_Id(@Param("boardGameId")Long boardGameId);

    Optional<AlternateName> findByName(String name);
}
