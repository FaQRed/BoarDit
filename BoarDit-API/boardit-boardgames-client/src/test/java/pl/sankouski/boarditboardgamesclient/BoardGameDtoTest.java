package pl.sankouski.boarditboardgamesclient;

import org.junit.jupiter.api.Test;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGameDtoTest {

    @Test
    public void getPrimaryName_shouldReturnCorrectName() {
        BoardGameDto.Name primaryName = new BoardGameDto.Name("primary", "Catan");
        BoardGameDto.Name alternateName = new BoardGameDto.Name("alternate", "Settlers of Catan");
        BoardGameDto dto = new BoardGameDto();
        dto.setNames(Arrays.asList(primaryName, alternateName));
        assertEquals("Catan", dto.getPrimaryName());
    }

    @Test
    public void getPrimaryName_shouldReturnNullIfNotFound() {
        BoardGameDto.Name alternateName = new BoardGameDto.Name("alternate", "Carcassonne");
        BoardGameDto dto = new BoardGameDto();
        dto.setNames(List.of(alternateName));
        assertNull(dto.getPrimaryName());
    }

    @Test
    public void getAlternateNames_shouldReturnCorrectList() {
        BoardGameDto.Name primaryName = new BoardGameDto.Name("primary", "Catan");
        BoardGameDto.Name alternate1 = new BoardGameDto.Name("alternate", "Settlers of Catan");
        BoardGameDto.Name alternate2 = new BoardGameDto.Name("alternate", "Катан");
        BoardGameDto dto = new BoardGameDto();
        dto.setNames(Arrays.asList(primaryName, alternate1, alternate2));
        List<String> alternates = dto.getAlternateNames();
        assertEquals(2, alternates.size());
        assertTrue(alternates.contains("Settlers of Catan"));
        assertTrue(alternates.contains("Катан"));
    }

    @Test
    public void getAlternateNames_shouldReturnEmptyListIfNone() {
        BoardGameDto.Name primaryName = new BoardGameDto.Name("primary", "Catan");
        BoardGameDto dto = new BoardGameDto();
        dto.setNames(List.of(primaryName));
        List<String> alternates = dto.getAlternateNames();
        assertTrue(alternates.isEmpty());
    }

    @Test
    public void gettersAndSetters_shouldWorkCorrectly() {
        BoardGameDto.YearPublished yearPublished = new BoardGameDto.YearPublished();
        BoardGameDto.MinPlayers minPlayers = new BoardGameDto.MinPlayers();
        BoardGameDto.MaxPlayers maxPlayers = new BoardGameDto.MaxPlayers();
        BoardGameDto.PlayingTime playingTime = new BoardGameDto.PlayingTime();
        List<BoardGameDto.Link> links = List.of(new BoardGameDto.Link("type", "value", "id"));
        BoardGameDto dto = new BoardGameDto(1L, null, "A strategy game", yearPublished, minPlayers, maxPlayers, playingTime, "image.jpg", links);
        dto.setBggId(1L);
        dto.setDescription("A strategy game");
        dto.setYearPublished(yearPublished);
        dto.setMinPlayers(minPlayers);
        dto.setMaxPlayers(maxPlayers);
        dto.setPlayingTime(playingTime);
        dto.setImageLink("image.jpg");
        dto.setLinks(links);
        dto.setNames(List.of());
        assertEquals(1L, dto.getBggId());
        assertEquals("A strategy game", dto.getDescription());
        assertEquals(yearPublished, dto.getYearPublished());
        assertEquals(minPlayers, dto.getMinPlayers());
        assertEquals(maxPlayers, dto.getMaxPlayers());
        assertEquals(playingTime, dto.getPlayingTime());
        assertEquals("image.jpg", dto.getImageLink());
        assertEquals(links, dto.getLinks());
    }
}