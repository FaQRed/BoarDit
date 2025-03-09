package pl.sankouski.boarditdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.sankouski.boarditdata.model.boardgame.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByBggId(Long bggId);
    boolean existsByName(String name);

    @Query("SELECT COUNT(bg) > 0 FROM BoardGame bg JOIN bg.categories c WHERE c.id = :categoryId")
    boolean isCategoryInUse(Long categoryId);

    boolean existsAllById(Long id);
}
