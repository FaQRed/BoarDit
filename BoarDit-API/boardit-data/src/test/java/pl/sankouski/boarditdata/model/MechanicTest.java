package pl.sankouski.boarditdata.model;

import org.junit.jupiter.api.Test;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.model.boardgame.Mechanic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MechanicTest {

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        Mechanic mechanic = new Mechanic();

        assertNull(mechanic.getId());
        assertNull(mechanic.getBggId());
        assertNull(mechanic.getName());
        assertNull(mechanic.getBoardGames());
    }

    @Test
    public void setId_shouldUpdateId() {
        Mechanic mechanic = new Mechanic();
        mechanic.setId(1L);

        assertEquals(1L, mechanic.getId());
    }

    @Test
    public void setBggId_shouldUpdateBggId() {
        Mechanic mechanic = new Mechanic();
        mechanic.setBggId(123L);

        assertEquals(123L, mechanic.getBggId());
    }

    @Test
    public void setName_shouldUpdateName() {
        Mechanic mechanic = new Mechanic();
        mechanic.setName("Card Drafting");

        assertEquals("Card Drafting", mechanic.getName());
    }

    @Test
    public void setBoardGames_shouldUpdateBoardGames() {
        Mechanic mechanic = new Mechanic();
        List<BoardGame> boardGames = new ArrayList<>();
        boardGames.add(new BoardGame());
        boardGames.add(new BoardGame());

        mechanic.setBoardGames(boardGames);

        assertEquals(boardGames, mechanic.getBoardGames());
    }

    @Test
    public void setId_shouldReturnSameInstance() {
        Mechanic mechanic = new Mechanic();
        Mechanic result = mechanic.setId(2L);

        assertSame(mechanic, result);
    }

    @Test
    public void setBggId_shouldReturnSameInstance() {
        Mechanic mechanic = new Mechanic();
        Mechanic result = mechanic.setBggId(456L);

        assertSame(mechanic, result);
    }

    @Test
    public void setName_shouldReturnSameInstance() {
        Mechanic mechanic = new Mechanic();
        Mechanic result = mechanic.setName("Worker Placement");

        assertSame(mechanic, result);
    }

    @Test
    public void setBoardGames_shouldReturnSameInstance() {
        Mechanic mechanic = new Mechanic();
        List<BoardGame> boardGames = new ArrayList<>();
        Mechanic result = mechanic.setBoardGames(boardGames);

        assertSame(mechanic, result);
    }
}