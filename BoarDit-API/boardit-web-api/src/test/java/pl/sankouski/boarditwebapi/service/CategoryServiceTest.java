package pl.sankouski.boarditwebapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sankouski.boarditdata.dto.CategoryDTO;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.model.boardgame.Category;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditdata.repository.CategoryRepository;
import pl.sankouski.boarditwebapi.service.boardgame.CategoryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BoardGameRepository boardGameRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository, boardGameRepository);
    }

    @Test
    void shouldGetAllCategories() {
        List<Category> categories = new ArrayList<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("Strategy");
        category.setBoardGames(new ArrayList<>());
        categories.add(category);
        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Adventure");
        category2.setBoardGames(new ArrayList<>());
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Strategy", result.get(0).getName());
        assertEquals("Adventure", result.get(1).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void shouldCreateCategory() {
        CategoryDTO categoryDTO = new CategoryDTO(null, "Puzzle");
        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Puzzle");
        savedCategory.setBoardGames(new ArrayList<>());

        when(categoryRepository.existsByName("Puzzle")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Puzzle", result.getName());
        verify(categoryRepository, times(1)).existsByName("Puzzle");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExists() {
        CategoryDTO categoryDTO = new CategoryDTO(null, "Puzzle");

        when(categoryRepository.existsByName("Puzzle")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(categoryDTO));
        verify(categoryRepository, times(1)).existsByName("Puzzle");
    }

    @Test
    void shouldDeleteCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Strategy");
        category.setBoardGames(new ArrayList<>());
        BoardGame boardGame = new BoardGame();
        boardGame.setCategories(new ArrayList<>(Collections.singletonList(category)));
        category.setBoardGames(List.of(boardGame));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.deleteCategory(1L);

        verify(boardGameRepository, times(1)).save(boardGame);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(1L));
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldGetCategoriesByIds() {
        List<Category> categories = new ArrayList<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("Strategy");
        category.setBoardGames(new ArrayList<>());
        categories.add(category);
        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Adventure");
        category2.setBoardGames(new ArrayList<>());
        categories.add(category2);

        when(categoryRepository.findAllById(List.of(1L, 2L))).thenReturn(categories);

        List<CategoryDTO> result = categoryService.getCategoriesByIds(List.of(1L, 2L));

        assertEquals(2, result.size());
        assertEquals("Strategy", result.get(0).getName());
        assertEquals("Adventure", result.get(1).getName());
        verify(categoryRepository, times(1)).findAllById(List.of(1L, 2L));
    }

    @Test
    void shouldUpdateCategory() {
        Category existingCategory = new Category();
        existingCategory.setBoardGames(new ArrayList<>());
        existingCategory.setId(1L);
        existingCategory.setName("Old Name");
        CategoryDTO updatedDTO = new CategoryDTO(1L, "Updated Name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));

        categoryService.updateCategory(1L, updatedDTO);

        assertEquals("Updated Name", existingCategory.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentCategory() {
        CategoryDTO updatedDTO = new CategoryDTO(1L, "Updated Name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(1L, updatedDTO));
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, never()).save(any());
    }
}