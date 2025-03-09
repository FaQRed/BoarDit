package pl.sankouski.boarditdata.model;

import org.junit.jupiter.api.Test;
import pl.sankouski.boarditdata.model.boardgame.*;

import static org.junit.jupiter.api.Assertions.*;

public class AlternateNameTest {

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        AlternateName alternateName = new AlternateName();

        assertNull(alternateName.getId());
        assertNull(alternateName.getName());
        assertNull(alternateName.getBoardGame());
    }

    @Test
    public void setId_shouldUpdateId() {
        AlternateName alternateName = new AlternateName();
        alternateName.setId(1L);

        assertEquals(1L, alternateName.getId());
    }

    @Test
    public void setName_shouldUpdateName() {
        AlternateName alternateName = new AlternateName();
        alternateName.setName("NewName");

        assertEquals("NewName", alternateName.getName());
    }

    @Test
    public void setBoardGame_shouldUpdateBoardGame() {
        AlternateName alternateName = new AlternateName();
        BoardGame boardGame = new BoardGame(); // Assuming BoardGame has a default constructor
        alternateName.setBoardGame(boardGame);

        assertEquals(boardGame, alternateName.getBoardGame());
    }

    @Test
    public void setId_shouldReturnSameInstance() {
        AlternateName alternateName = new AlternateName();
        AlternateName result = alternateName.setId(2L);

        assertSame(alternateName, result);
    }

    @Test
    public void setName_shouldReturnSameInstance() {
        AlternateName alternateName = new AlternateName();
        AlternateName result = alternateName.setName("AnotherName");

        assertSame(alternateName, result);
    }

    @Test
    public void setBoardGame_shouldReturnSameInstance() {
        AlternateName alternateName = new AlternateName();
        BoardGame boardGame = new BoardGame();
        AlternateName result = alternateName.setBoardGame(boardGame);

        assertSame(alternateName, result);
    }
}