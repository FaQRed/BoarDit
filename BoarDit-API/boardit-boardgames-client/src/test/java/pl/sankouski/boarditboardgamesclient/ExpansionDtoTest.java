package pl.sankouski.boarditboardgamesclient;

import org.junit.jupiter.api.Test;
import pl.sankouski.boarditboardgamesclient.dto.ExpansionDto;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpansionDtoTest {

    @Test
    public void getPrimaryName_shouldReturnCorrectPrimaryName() {
        ExpansionDto.Name primaryName = new ExpansionDto.Name("primary", "Catan: Expansion");
        ExpansionDto.Name alternateName = new ExpansionDto.Name("alternate", "Settlers of Catan: Expansion");
        ExpansionDto dto = new ExpansionDto();
        dto.setNames(Arrays.asList(primaryName, alternateName));
        assertEquals("Catan: Expansion", dto.getPrimaryName());
    }

    @Test
    public void getPrimaryName_shouldReturnNullIfNoPrimaryName() {
        ExpansionDto.Name alternateName = new ExpansionDto.Name("alternate", "Carcassonne: Expansion");
        ExpansionDto dto = new ExpansionDto();
        dto.setNames(List.of(alternateName));
        assertNull(dto.getPrimaryName());
    }

    @Test
    public void gettersAndSetters_shouldSetAndGetValuesCorrectly() {
        ExpansionDto.YearPublished yearPublished = new ExpansionDto.YearPublished();
        ExpansionDto.MinPlayers minPlayers = new ExpansionDto.MinPlayers();
        ExpansionDto.MaxPlayers maxPlayers = new ExpansionDto.MaxPlayers();
        ExpansionDto.PlayingTime playingTime = new ExpansionDto.PlayingTime();
        List<ExpansionDto.Name> names = List.of(new ExpansionDto.Name("primary", "Catan: Expansion"));
        String description = "An exciting expansion for Catan";
        String imageLink = "http://example.com/image.jpg";
        Long bggId = 123L;

        ExpansionDto dto = new ExpansionDto();
        dto.setBggId(bggId);
        dto.setNames(names);
        dto.setDescription(description);
        dto.setYearPublished(yearPublished);
        dto.setMinPlayers(minPlayers);
        dto.setMaxPlayers(maxPlayers);
        dto.setPlayingTime(playingTime);
        dto.setImageLink(imageLink);

        assertEquals(bggId, dto.getBggId());
        assertEquals(names, dto.getNames());
        assertEquals(description, dto.getDescription());
        assertEquals(yearPublished, dto.getYearPublished());
        assertEquals(minPlayers, dto.getMinPlayers());
        assertEquals(maxPlayers, dto.getMaxPlayers());
        assertEquals(playingTime, dto.getPlayingTime());
        assertEquals(imageLink, dto.getImageLink());
    }


}