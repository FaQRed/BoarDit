package pl.sankouski.boarditdata.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDTOTest {

    @Test
    public void constructorWithParameters_shouldInitializeFields() {
        CategoryDTO dto = new CategoryDTO(1L, "CategoryName");

        assertEquals(1L, dto.getId());
        assertEquals("CategoryName", dto.getName());
    }

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        CategoryDTO dto = new CategoryDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
    }

    @Test
    public void setId_shouldUpdateId() {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(123L);

        assertEquals(123L, dto.getId());
    }

    @Test
    public void setName_shouldUpdateName() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("UpdatedCategory");

        assertEquals("UpdatedCategory", dto.getName());
    }
}