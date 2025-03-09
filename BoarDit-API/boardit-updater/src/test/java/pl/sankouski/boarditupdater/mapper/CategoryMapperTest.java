package pl.sankouski.boarditupdater.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto.Link;
import pl.sankouski.boarditdata.model.boardgame.Category;
import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;
import pl.sankouski.boarditdata.repository.CategoryRepository;
import pl.sankouski.boarditupdater.boardGames.mapper.CategoryMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryMapperTest {

    @Mock
    private BoarDitDataCatalog boarDitDataCatalog;

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryMapper = new CategoryMapper();

        when(boarDitDataCatalog.getCategories()).thenReturn(categoryRepository);
    }

    @Test
    void mapCategory_shouldReturnExistingCategory() {
        BoardGameDto dto = createTestBoardGameDto();
        Category existingCategory = new Category().setName("Category 1").setBggId(123L);

        when(categoryRepository.findByBggId(123L)).thenReturn(Optional.of(existingCategory));

        List<Category> categories = categoryMapper.mapCategory(dto, boarDitDataCatalog);

        assertEquals(1, categories.size());
        assertEquals(existingCategory, categories.get(0));

        verify(categoryRepository, times(1)).findByBggId(123L);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void mapCategory_shouldSaveAndReturnNewCategory() {
        BoardGameDto dto = createTestBoardGameDto();
        Category newCategory = new Category().setName("Category 1").setBggId(123L);

        when(categoryRepository.findByBggId(123L)).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        List<Category> categories = categoryMapper.mapCategory(dto, boarDitDataCatalog);

        assertEquals(1, categories.size());
        assertEquals("Category 1", categories.get(0).getName());
        assertEquals(123L, categories.get(0).getBggId());

        verify(categoryRepository, times(1)).findByBggId(123L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    private BoardGameDto createTestBoardGameDto() {
        BoardGameDto boardGameDto = new BoardGameDto();

        boardGameDto.setLinks(List.of(new Link("boardgamecategory", "Category 1", "123")));
        return boardGameDto;
    }
}