package pl.sankouski.boarditdata.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArtistDTOTest {

    @Test
    public void constructor_shouldInitializeFields() {
        ArtistDTO dto = new ArtistDTO(1L, "ArtistName");

        assertEquals(1L, dto.getId());
        assertEquals("ArtistName", dto.getName());
    }

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        ArtistDTO dto = new ArtistDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
    }

    @Test
    public void setId_shouldUpdateId() {
        ArtistDTO dto = new ArtistDTO();
        dto.setId(123L);

        assertEquals(123L, dto.getId());
    }

    @Test
    public void setName_shouldUpdateName() {
        ArtistDTO dto = new ArtistDTO();
        dto.setName("NewArtist");

        assertEquals("NewArtist", dto.getName());
    }
}