package pl.sankouski.boarditupdater.mapper;

import org.junit.jupiter.api.Test;
import pl.sankouski.boarditboardgamesclient.dto.ExpansionDto;
import pl.sankouski.boarditdata.model.boardgame.Expansion;
import pl.sankouski.boarditupdater.boardGames.mapper.ExpansionMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpansionMapperTest {

    @Test
    void mapExpansions_shouldMapDtoToEntity() {
        ExpansionDto dto = createTestExpansionDto();

        Expansion expansion = ExpansionMapper.mapExpansions(dto);

        assertEquals(dto.getBggId(), expansion.getBggId());
        assertEquals(dto.getPrimaryName(), expansion.getName());
        assertEquals(dto.getDescription(), expansion.getDescription());
        assertEquals(dto.getYearPublished().getValue(), expansion.getYearPublished());
        assertEquals(dto.getMinPlayers().getValue(), expansion.getMinPlayers());
        assertEquals(dto.getMaxPlayers().getValue(), expansion.getMaxPlayers());
        assertEquals(dto.getPlayingTime().getValue(), expansion.getPlayingTime());
        assertEquals(dto.getImageLink(), expansion.getImageLink());
    }

    private ExpansionDto createTestExpansionDto() {
        return new ExpansionDto(
                123L,
                List.of(new ExpansionDto.Name("primary", "Test Expansion")),
                "Test description",
                new ExpansionDto.YearPublished() {{ setValue(2021); }},
                new ExpansionDto.MinPlayers() {{ setValue(2); }},
                new ExpansionDto.MaxPlayers() {{ setValue(4); }},
                new ExpansionDto.PlayingTime() {{ setValue(60); }},
                "http://test.image/link"
        );
    }
}