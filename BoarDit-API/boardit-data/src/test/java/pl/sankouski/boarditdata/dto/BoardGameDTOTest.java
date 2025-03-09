package pl.sankouski.boarditdata.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGameDTOTest {

    @Test
    public void constructor_shouldInitializeAllFields() {
        List<Long> categories = List.of(1L, 2L);
        List<Long> mechanics = List.of(3L, 4L);
        List<String> artists = List.of("Artist1", "Artist2");
        List<String> alternateNames = List.of("AltName1", "AltName2");

        BoardGameDTO dto = new BoardGameDTO(
                1L,
                "Catan",
                "A fun board game",
                1995,
                3,
                4,
                120,
                "http://example.com/image.jpg",
                categories,
                mechanics,
                artists,
                alternateNames
        );

        assertEquals(1L, dto.getId());
        assertEquals("Catan", dto.getName());
        assertEquals("A fun board game", dto.getDescription());
        assertEquals(1995, dto.getYearPublished());
        assertEquals(3, dto.getMinPlayers());
        assertEquals(4, dto.getMaxPlayers());
        assertEquals(120, dto.getPlayingTime());
        assertEquals("http://example.com/image.jpg", dto.getImageLink());
        assertEquals(categories, dto.getCategories());
        assertEquals(mechanics, dto.getMechanics());
        assertEquals(artists, dto.getArtists());
        assertEquals(alternateNames, dto.getAlternateNames());
    }

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        BoardGameDTO dto = new BoardGameDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getDescription());
        assertEquals(0, dto.getYearPublished());
        assertEquals(0, dto.getMinPlayers());
        assertEquals(0, dto.getMaxPlayers());
        assertEquals(0, dto.getPlayingTime());
        assertNull(dto.getImageLink());
        assertNull(dto.getCategories());
        assertNull(dto.getMechanics());
        assertNull(dto.getArtists());
        assertNull(dto.getAlternateNames());
    }

    @Test
    public void settersAndGetters_shouldSetAndGetValuesCorrectly() {
        BoardGameDTO dto = new BoardGameDTO();

        dto.setId(1L)
                .setName("Catan")
                .setDescription("A fun board game")
                .setYearPublished(1995)
                .setMinPlayers(3)
                .setMaxPlayers(4)
                .setPlayingTime(120)
                .setImageLink("http://example.com/image.jpg")
                .setCategories(List.of(1L, 2L))
                .setMechanics(List.of(3L, 4L))
                .setArtists(List.of("Artist1", "Artist2"))
                .setAlternateNames(List.of("AltName1", "AltName2"));

        assertEquals(1L, dto.getId());
        assertEquals("Catan", dto.getName());
        assertEquals("A fun board game", dto.getDescription());
        assertEquals(1995, dto.getYearPublished());
        assertEquals(3, dto.getMinPlayers());
        assertEquals(4, dto.getMaxPlayers());
        assertEquals(120, dto.getPlayingTime());
        assertEquals("http://example.com/image.jpg", dto.getImageLink());
        assertEquals(List.of(1L, 2L), dto.getCategories());
        assertEquals(List.of(3L, 4L), dto.getMechanics());
        assertEquals(List.of("Artist1", "Artist2"), dto.getArtists());
        assertEquals(List.of("AltName1", "AltName2"), dto.getAlternateNames());
    }
}