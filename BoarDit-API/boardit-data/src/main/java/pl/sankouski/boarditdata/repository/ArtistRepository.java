package pl.sankouski.boarditdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.sankouski.boarditdata.model.boardgame.Artist;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByBggId(Long bggId);

    boolean existsByName(String name);

    @Query("SELECT COUNT(bg) > 0 FROM BoardGame bg JOIN bg.artists a WHERE a.id = :artistId")
    boolean isArtistInUse(Long artistId);

    Optional<Artist> findByName(String name);
}
