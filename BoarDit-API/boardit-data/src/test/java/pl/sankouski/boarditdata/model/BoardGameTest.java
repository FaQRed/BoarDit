package pl.sankouski.boarditdata.model;

import org.junit.jupiter.api.Test;
import pl.sankouski.boarditdata.model.boardgame.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGameTest {

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        BoardGame boardGame = new BoardGame();

        assertNull(boardGame.getId());
        assertNull(boardGame.getName());
        assertNull(boardGame.getDescription());
        assertEquals(0, boardGame.getYearPublished());
        assertEquals(0, boardGame.getMinPlayers());
        assertEquals(0, boardGame.getMaxPlayers());
        assertEquals(0, boardGame.getPlayingTime());
        assertNull(boardGame.getImageLink());
        assertNull(boardGame.getBggId());
        assertNull(boardGame.getAlternateNames());
        assertNull(boardGame.getCategories());
        assertNull(boardGame.getMechanics());
        assertNull(boardGame.getExpansions());
        assertNull(boardGame.getArtists());
    }

    @Test
    public void setId_shouldUpdateId() {
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);

        assertEquals(1L, boardGame.getId());
    }

    @Test
    public void setName_shouldUpdateName() {
        BoardGame boardGame = new BoardGame();
        boardGame.setName("Catan");

        assertEquals("Catan", boardGame.getName());
    }

    @Test
    public void setDescription_shouldUpdateDescription() {
        BoardGame boardGame = new BoardGame();
        boardGame.setDescription("A strategy game");

        assertEquals("A strategy game", boardGame.getDescription());
    }

    @Test
    public void setYearPublished_shouldUpdateYearPublished() {
        BoardGame boardGame = new BoardGame();
        boardGame.setYearPublished(1995);

        assertEquals(1995, boardGame.getYearPublished());
    }

    @Test
    public void setMinPlayers_shouldUpdateMinPlayers() {
        BoardGame boardGame = new BoardGame();
        boardGame.setMinPlayers(2);

        assertEquals(2, boardGame.getMinPlayers());
    }

    @Test
    public void setMaxPlayers_shouldUpdateMaxPlayers() {
        BoardGame boardGame = new BoardGame();
        boardGame.setMaxPlayers(4);

        assertEquals(4, boardGame.getMaxPlayers());
    }

    @Test
    public void setPlayingTime_shouldUpdatePlayingTime() {
        BoardGame boardGame = new BoardGame();
        boardGame.setPlayingTime(60);

        assertEquals(60, boardGame.getPlayingTime());
    }

    @Test
    public void setImageLink_shouldUpdateImageLink() {
        BoardGame boardGame = new BoardGame();
        boardGame.setImageLink("http://example.com/image.jpg");

        assertEquals("http://example.com/image.jpg", boardGame.getImageLink());
    }

    @Test
    public void setBggId_shouldUpdateBggId() {
        BoardGame boardGame = new BoardGame();
        boardGame.setBggId(123L);

        assertEquals(123L, boardGame.getBggId());
    }

    @Test
    public void setAlternateNames_shouldUpdateAlternateNames() {
        BoardGame boardGame = new BoardGame();
        List<AlternateName> alternateNames = new ArrayList<>();
        alternateNames.add(new AlternateName());
        boardGame.setAlternateNames(alternateNames);

        assertEquals(alternateNames, boardGame.getAlternateNames());
    }

    @Test
    public void setCategories_shouldUpdateCategories() {
        BoardGame boardGame = new BoardGame();
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        boardGame.setCategories(categories);

        assertEquals(categories, boardGame.getCategories());
    }

    @Test
    public void setMechanics_shouldUpdateMechanics() {
        BoardGame boardGame = new BoardGame();
        List<Mechanic> mechanics = new ArrayList<>();
        mechanics.add(new Mechanic());
        boardGame.setMechanics(mechanics);

        assertEquals(mechanics, boardGame.getMechanics());
    }

    @Test
    public void setExpansions_shouldUpdateExpansions() {
        BoardGame boardGame = new BoardGame();
        List<Expansion> expansions = new ArrayList<>();
        expansions.add(new Expansion());
        boardGame.setExpansions(expansions);

        assertEquals(expansions, boardGame.getExpansions());
    }

    @Test
    public void setArtists_shouldUpdateArtists() {
        BoardGame boardGame = new BoardGame();
        List<Artist> artists = new ArrayList<>();
        artists.add(new Artist());
        boardGame.setArtists(artists);

        assertEquals(artists, boardGame.getArtists());
    }
}