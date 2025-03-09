package pl.sankouski.boarditdata.model;

import org.junit.jupiter.api.Test;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.model.boardgame.Expansion;

import static org.junit.jupiter.api.Assertions.*;

public class ExpansionTest {

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        Expansion expansion = new Expansion();

        assertNull(expansion.getId());
        assertNull(expansion.getBggId());
        assertNull(expansion.getName());
        assertNull(expansion.getDescription());
        assertEquals(0, expansion.getYearPublished());
        assertEquals(0, expansion.getMinPlayers());
        assertEquals(0, expansion.getMaxPlayers());
        assertEquals(0, expansion.getPlayingTime());
        assertNull(expansion.getImageLink());
        assertNull(expansion.getBoardGame());
    }

    @Test
    public void setId_shouldUpdateId() {
        Expansion expansion = new Expansion();
        expansion.setId(1L);

        assertEquals(1L, expansion.getId());
    }

    @Test
    public void setBggId_shouldUpdateBggId() {
        Expansion expansion = new Expansion();
        expansion.setBggId(123L);

        assertEquals(123L, expansion.getBggId());
    }

    @Test
    public void setName_shouldUpdateName() {
        Expansion expansion = new Expansion();
        expansion.setName("ExpansionName");

        assertEquals("ExpansionName", expansion.getName());
    }

    @Test
    public void setDescription_shouldUpdateDescription() {
        Expansion expansion = new Expansion();
        expansion.setDescription("A detailed expansion description");

        assertEquals("A detailed expansion description", expansion.getDescription());
    }

    @Test
    public void setYearPublished_shouldUpdateYearPublished() {
        Expansion expansion = new Expansion();
        expansion.setYearPublished(2021);

        assertEquals(2021, expansion.getYearPublished());
    }

    @Test
    public void setMinPlayers_shouldUpdateMinPlayers() {
        Expansion expansion = new Expansion();
        expansion.setMinPlayers(2);

        assertEquals(2, expansion.getMinPlayers());
    }

    @Test
    public void setMaxPlayers_shouldUpdateMaxPlayers() {
        Expansion expansion = new Expansion();
        expansion.setMaxPlayers(5);

        assertEquals(5, expansion.getMaxPlayers());
    }

    @Test
    public void setPlayingTime_shouldUpdatePlayingTime() {
        Expansion expansion = new Expansion();
        expansion.setPlayingTime(60);

        assertEquals(60, expansion.getPlayingTime());
    }

    @Test
    public void setImageLink_shouldUpdateImageLink() {
        Expansion expansion = new Expansion();
        expansion.setImageLink("http://example.com/expansion.jpg");

        assertEquals("http://example.com/expansion.jpg", expansion.getImageLink());
    }

    @Test
    public void setBoardGame_shouldUpdateBoardGame() {
        Expansion expansion = new Expansion();
        BoardGame boardGame = new BoardGame();
        expansion.setBoardGame(boardGame);

        assertEquals(boardGame, expansion.getBoardGame());
    }

    @Test
    public void setId_shouldReturnSameInstance() {
        Expansion expansion = new Expansion();
        Expansion result = expansion.setId(2L);

        assertSame(expansion, result);
    }

    @Test
    public void setBggId_shouldReturnSameInstance() {
        Expansion expansion = new Expansion();
        Expansion result = expansion.setBggId(456L);

        assertSame(expansion, result);
    }

    @Test
    public void setName_shouldReturnSameInstance() {
        Expansion expansion = new Expansion();
        Expansion result = expansion.setName("AnotherExpansion");

        assertSame(expansion, result);
    }

    @Test
    public void setDescription_shouldReturnSameInstance() {
        Expansion expansion = new Expansion();
        Expansion result = expansion.setDescription("Another description");

        assertSame(expansion, result);
    }

    @Test
    public void setBoardGame_shouldReturnSameInstance() {
        Expansion expansion = new Expansion();
        BoardGame boardGame = new BoardGame();
        Expansion result = expansion.setBoardGame(boardGame);

        assertSame(expansion, result);
    }
}