package pl.sankouski.boarditdata.model;


import org.junit.jupiter.api.Test;
import pl.sankouski.boarditdata.model.boardgame.Artist;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArtistTest {

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        Artist artist = new Artist();

        assertNull(artist.getId());
        assertNull(artist.getBggId());
        assertNull(artist.getName());
        assertNotNull(artist.getBoardGames());
        assertTrue(artist.getBoardGames().isEmpty());
    }

    @Test
    public void constructorWithParameters_shouldInitializeFields() {
        List<BoardGame> boardGames = List.of(new BoardGame(), new BoardGame());
        Artist artist = new Artist("ArtistName", boardGames);

        assertEquals("ArtistName", artist.getName());
        assertEquals(boardGames, artist.getBoardGames());
    }

    @Test
    public void setId_shouldUpdateId() {
        Artist artist = new Artist();
        artist.setId(1L);

        assertEquals(1L, artist.getId());
    }

    @Test
    public void setBggId_shouldUpdateBggId() {
        Artist artist = new Artist();
        artist.setBggId(123L);

        assertEquals(123L, artist.getBggId());
    }

    @Test
    public void setName_shouldUpdateName() {
        Artist artist = new Artist();
        artist.setName("NewArtistName");

        assertEquals("NewArtistName", artist.getName());
    }

    @Test
    public void setBoardGames_shouldUpdateBoardGames() {
        Artist artist = new Artist();
        List<BoardGame> boardGames = new ArrayList<>();
        boardGames.add(new BoardGame());
        boardGames.add(new BoardGame());
        artist.setBoardGames(boardGames);

        assertEquals(boardGames, artist.getBoardGames());
    }

    @Test
    public void setId_shouldReturnSameInstance() {
        Artist artist = new Artist();
        Artist result = artist.setId(2L);

        assertSame(artist, result);
    }

    @Test
    public void setBggId_shouldReturnSameInstance() {
        Artist artist = new Artist();
        Artist result = artist.setBggId(456L);

        assertSame(artist, result);
    }

    @Test
    public void setName_shouldReturnSameInstance() {
        Artist artist = new Artist();
        Artist result = artist.setName("AnotherName");

        assertSame(artist, result);
    }

    @Test
    public void setBoardGames_shouldReturnSameInstance() {
        Artist artist = new Artist();
        List<BoardGame> boardGames = new ArrayList<>();
        Artist result = artist.setBoardGames(boardGames);

        assertSame(artist, result);
    }
}