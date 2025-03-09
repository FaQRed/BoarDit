package pl.sankouski.boarditdata.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlternateNameDTOTest {

    @Test
    public void constructor_shouldInitializeFields() {
        AlternateNameDTO dto = new AlternateNameDTO("TestName", 123L);

        assertEquals("TestName", dto.getName());
        assertEquals(123L, dto.getId());
    }

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        AlternateNameDTO dto = new AlternateNameDTO();

        assertNull(dto.getName());
        assertEquals(0L, dto.getId());
    }

    @Test
    public void setName_shouldUpdateName() {
        AlternateNameDTO dto = new AlternateNameDTO();
        dto.setName("NewName");

        assertEquals("NewName", dto.getName());
    }

    @Test
    public void setId_shouldUpdateId() {
        AlternateNameDTO dto = new AlternateNameDTO();
        dto.setId(456L);

        assertEquals(456L, dto.getId());
    }

    @Test
    public void setId_shouldReturnSameInstance() {
        AlternateNameDTO dto = new AlternateNameDTO();
        AlternateNameDTO result = dto.setId(789L);

        assertSame(dto, result);
    }
}