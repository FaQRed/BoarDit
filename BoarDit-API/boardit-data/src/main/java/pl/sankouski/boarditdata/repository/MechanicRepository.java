package pl.sankouski.boarditdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.sankouski.boarditdata.model.boardgame.Mechanic;

import java.util.Optional;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    Optional<Mechanic> findByBggId(Long bggId);
    boolean existsByName(String name);

    @Query("SELECT COUNT(bg) > 0 FROM BoardGame bg JOIN bg.mechanics m WHERE m.id = :mechanicId")
    boolean isMechanicInUse(Long mechanicId);
}
