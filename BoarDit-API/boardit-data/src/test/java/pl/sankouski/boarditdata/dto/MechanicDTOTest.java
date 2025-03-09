package pl.sankouski.boarditdata.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MechanicDTOTest {

    @Test
    public void constructorWithParameters_shouldInitializeFields() {
        MechanicDTO dto = new MechanicDTO(1L, "MechanicName");

        assertEquals(1L, dto.getId());
        assertEquals("MechanicName", dto.getName());
    }

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        MechanicDTO dto = new MechanicDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
    }

    @Test
    public void setId_shouldUpdateId() {
        MechanicDTO dto = new MechanicDTO();
        dto.setId(123L);

        assertEquals(123L, dto.getId());
    }

    @Test
    public void setName_shouldUpdateName() {
        MechanicDTO dto = new MechanicDTO();
        dto.setName("UpdatedMechanic");

        assertEquals("UpdatedMechanic", dto.getName());
    }

    @Test
    public void setId_shouldReturnSameInstance() {
        MechanicDTO dto = new MechanicDTO();
        MechanicDTO result = dto.setId(456L);

        assertSame(dto, result);
    }

    @Test
    public void setName_shouldReturnSameInstance() {
        MechanicDTO dto = new MechanicDTO();
        MechanicDTO result = dto.setName("AnotherName");

        assertSame(dto, result);
    }
}