package pl.sankouski.boarditwebapi.service.boardgame;

import org.springframework.stereotype.Service;
import pl.sankouski.boarditdata.dto.CategoryDTO;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.model.boardgame.Category;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditdata.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BoardGameRepository boardGameRepository;

    public CategoryService(CategoryRepository categoryRepository, BoardGameRepository boardGameRepository) {
        this.categoryRepository = categoryRepository;
        this.boardGameRepository = boardGameRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with name " + categoryDTO.getName() + " already exists.");
        }
        Category category = new Category();
        category.setName(categoryDTO.getName());
        Category savedCategory = categoryRepository.save(category);
        return new CategoryDTO(savedCategory.getId(), savedCategory.getName());
    }

    public void deleteCategory(Long id) {
Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Category with ID " + id + " not found."));
        for (BoardGame boardGame : category.getBoardGames()) {
            boardGame.getCategories().remove(category);
            boardGameRepository.save(boardGame);
        }

        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category with ID " + id + " does not exist.");
        }
        categoryRepository.deleteById(id);
    }

    public List<CategoryDTO> getCategoriesByIds(List<Long> ids) {
        return categoryRepository.findAllById(ids).stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    public void updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));

        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
    }
}