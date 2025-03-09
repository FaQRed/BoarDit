package pl.sankouski.boarditdata.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpansionDTOTest {

    @Test
    public void constructorWithParameters_shouldInitializeFields() {
        ExpansionDTO dto = new ExpansionDTO(
                1L,
                "ExpansionName",
                "An expansion description",
                2020,
                2,
                5,
                60,
                "http://example.com/image.jpg"
        );

        assertEquals(1L, dto.getId());
        assertEquals("ExpansionName", dto.getName());
        assertEquals("An expansion description", dto.getDescription());
        assertEquals(2020, dto.getYearPublished());
        assertEquals(2, dto.getMinPlayers());
        assertEquals(5, dto.getMaxPlayers());
        assertEquals(60, dto.getPlayingTime());
        assertEquals("http://example.com/image.jpg", dto.getImageLink());
    }

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        ExpansionDTO dto = new ExpansionDTO();

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
        ExpansionDTO dto = new ExpansionDTO();

        dto.setId(2L)
                .setName("UpdatedExpansion")
                .setDescription("Updated description")
                .setYearPublished(2021)
                .setMinPlayers(3)
                .setMaxPlayers(6)
                .setPlayingTime(90)
                .setImageLink("http://example.com/updated-image.jpg");

        assertEquals(2L, dto.getId());
        assertEquals("UpdatedExpansion", dto.getName());
        assertEquals("Updated description", dto.getDescription());
        assertEquals(2021, dto.getYearPublished());
        assertEquals(3, dto.getMinPlayers());
        assertEquals(6, dto.getMaxPlayers());
        assertEquals(90, dto.getPlayingTime());
        assertEquals("http://example.com/updated-image.jpg", dto.getImageLink());
    }

    @Test
    public void setId_shouldReturnSameInstance() {
        ExpansionDTO dto = new ExpansionDTO();
        ExpansionDTO result = dto.setId(3L);

        assertSame(dto, result);
    }

    @Test
    public void setName_shouldReturnSameInstance() {
        ExpansionDTO dto = new ExpansionDTO();
        ExpansionDTO result = dto.setName("AnotherName");

        assertSame(dto, result);
    }
}