package pl.sankouski.boarditdata.model;


import org.junit.jupiter.api.Test;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.model.boardgame.Category;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        Category category = new Category();

        assertNull(category.getId());
        assertNull(category.getBggId());
        assertNull(category.getName());
        assertNull(category.getBoardGames());
    }

    @Test
    public void setId_shouldUpdateId() {
        Category category = new Category();
        category.setId(1L);

        assertEquals(1L, category.getId());
    }

    @Test
    public void setBggId_shouldUpdateBggId() {
        Category category = new Category();
        category.setBggId(123L);

        assertEquals(123L, category.getBggId());
    }

    @Test
    public void setName_shouldUpdateName() {
        Category category = new Category();
        category.setName("CategoryName");

        assertEquals("CategoryName", category.getName());
    }

    @Test
    public void setBoardGames_shouldUpdateBoardGames() {
        Category category = new Category();
        List<BoardGame> boardGames = new ArrayList<>();
        boardGames.add(new BoardGame());
        boardGames.add(new BoardGame());

        category.setBoardGames(boardGames);

        assertEquals(boardGames, category.getBoardGames());
    }

    @Test
    public void setId_shouldReturnSameInstance() {
        Category category = new Category();
        Category result = category.setId(2L);

        assertSame(category, result);
    }

    @Test
    public void setBggId_shouldReturnSameInstance() {
        Category category = new Category();
        Category result = category.setBggId(456L);

        assertSame(category, result);
    }

    @Test
    public void setName_shouldReturnSameInstance() {
        Category category = new Category();
        Category result = category.setName("AnotherCategory");

        assertSame(category, result);
    }

    @Test
    public void setBoardGames_shouldReturnSameInstance() {
        Category category = new Category();
        List<BoardGame> boardGames = new ArrayList<>();
        Category result = category.setBoardGames(boardGames);

        assertSame(category, result);
    }
}