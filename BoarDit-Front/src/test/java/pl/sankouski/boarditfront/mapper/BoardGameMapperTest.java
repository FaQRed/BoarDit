package pl.sankouski.boarditfront.mapper;
import org.junit.jupiter.api.Test;
import pl.sankouski.boarditfront.dto.BoardGameDTO;
import pl.sankouski.boarditfront.dto.BoardGameSummaryDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardGameMapperTest {

    @Test
    void testToSummary_NullInput() {
        BoardGameSummaryDto result = BoardGameMapper.toSummary(null);

        assertNull(result);
    }

    @Test
    void testToSummary_ValidInput() {

        BoardGameDTO dto = new BoardGameDTO();
        dto.setId(1L);
        dto.setName("Catan");
        dto.setImageLink("http://example.com/catan.jpg");

        BoardGameSummaryDto result = BoardGameMapper.toSummary(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getImageLink(), result.getImageUrl());
    }

    @Test
    void testToSummaryList_NullInput() {
        List<BoardGameSummaryDto> result = BoardGameMapper.toSummaryList(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToSummaryList_EmptyList() {
        List<BoardGameSummaryDto> result = BoardGameMapper.toSummaryList(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToSummaryList_ValidInput() {
        BoardGameDTO dto1 = new BoardGameDTO();
        dto1.setId(1L);
        dto1.setName("Catan");
        dto1.setImageLink("http://example.com/catan.jpg");

        BoardGameDTO dto2 = new BoardGameDTO();
        dto2.setId(2L);
        dto2.setName("Monopoly");
        dto2.setImageLink("http://example.com/monopoly.jpg");

        List<BoardGameDTO> dtoList = List.of(dto1, dto2);
        List<BoardGameSummaryDto> result = BoardGameMapper.toSummaryList(dtoList);
        assertNotNull(result);
        assertEquals(2, result.size());
        BoardGameSummaryDto summary1 = result.get(0);
        BoardGameSummaryDto summary2 = result.get(1);

        assertEquals(dto1.getId(), summary1.getId());
        assertEquals(dto1.getName(), summary1.getName());
        assertEquals(dto1.getImageLink(), summary1.getImageUrl());

        assertEquals(dto2.getId(), summary2.getId());
        assertEquals(dto2.getName(), summary2.getName());
        assertEquals(dto2.getImageLink(), summary2.getImageUrl());
    }
}