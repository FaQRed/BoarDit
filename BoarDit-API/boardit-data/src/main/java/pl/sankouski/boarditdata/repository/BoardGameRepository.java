package pl.sankouski.boarditdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;

import java.util.Optional;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {
    Optional<BoardGame> findByBggId(Long bggId);

    Optional<Object> findByName(String name);
}
