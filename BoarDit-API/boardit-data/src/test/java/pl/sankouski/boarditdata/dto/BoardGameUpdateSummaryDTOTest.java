package pl.sankouski.boarditdata.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGameUpdateSummaryDTOTest {

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        BoardGameUpdateSummaryDTO dto = new BoardGameUpdateSummaryDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getDescription());
        assertEquals(0, dto.getYearPublished());
        assertEquals(0, dto.getMinPlayers());
        assertEquals(0, dto.getMaxPlayers());
        assertEquals(0, dto.getPlayingTime());
        assertNull(dto.getImageLink());
    }

    @Test
    public void settersAndGetters_shouldSetAndGetValuesCorrectly() {
        BoardGameUpdateSummaryDTO dto = new BoardGameUpdateSummaryDTO();

        dto.setId(1L)
                .setName("Catan")
                .setDescription("An updated board game description")
                .setYearPublished(2020)
                .setMinPlayers(2)
                .setMaxPlayers(4)
                .setPlayingTime(90)
                .setImageLink("http://example.com/image.jpg");

        assertEquals(1L, dto.getId());
        assertEquals("Catan", dto.getName());
        assertEquals("An updated board game description", dto.getDescription());
        assertEquals(2020, dto.getYearPublished());
        assertEquals(2, dto.getMinPlayers());
        assertEquals(4, dto.getMaxPlayers());
        assertEquals(90, dto.getPlayingTime());
        assertEquals("http://example.com/image.jpg", dto.getImageLink());
    }

    @Test
    public void setId_shouldReturnSameInstance() {
        BoardGameUpdateSummaryDTO dto = new BoardGameUpdateSummaryDTO();

        BoardGameUpdateSummaryDTO result = dto.setId(1L);

        assertSame(dto, result);
    }

    @Test
    public void setName_shouldReturnSameInstance() {
        BoardGameUpdateSummaryDTO dto = new BoardGameUpdateSummaryDTO();

        BoardGameUpdateSummaryDTO result = dto.setName("Updated Name");

        assertSame(dto, result);
    }
}