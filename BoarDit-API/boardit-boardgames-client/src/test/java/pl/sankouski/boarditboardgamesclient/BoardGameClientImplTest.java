package pl.sankouski.boarditboardgamesclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditboardgamesclient.dto.ExpansionDto;
import pl.sankouski.boarditboardgamesclient.dto.ItemsBoardGame;
import pl.sankouski.boarditboardgamesclient.dto.ItemsExpansion;
import pl.sankouski.boarditboardgamesclient.exception.BoardGameNotFoundException;
import pl.sankouski.boarditboardgamesclient.uriBuilder.BoardGamesClientUriBuilderProvider;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BoardGameClientImplTest {

    private BoardGameClientImpl client;
    private RestTemplate restTemplateMock;
    private BoardGamesClientUriBuilderProvider uriBuilderMock;

    @BeforeEach
    public void setup() {
        restTemplateMock = mock(RestTemplate.class);
        uriBuilderMock = mock(BoardGamesClientUriBuilderProvider.class);
        client = new BoardGameClientImpl(uriBuilderMock);
        client.restClient = restTemplateMock;
    }

    @Test
    public void getBoardGameByIdFromApi_shouldReturnBoardGameDto() throws JAXBException {
        String sampleXmlResponse = "<items><item><name type=\"primary\" value=\"Catan\"/></item></items>";
        Long boardGameId = 1L;

        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn(sampleXmlResponse);

        BoardGameDto boardGame = client.getBoardGameByIdFromApi(boardGameId);

        assertNotNull(boardGame);
        assertEquals(boardGameId, boardGame.getBggId());
        verify(restTemplateMock, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    public void getBoardGameByIdFromApi_shouldThrowBoardGameNotFoundException() {
        Long boardGameId = 1L;
        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn(null);

        assertThrows(BoardGameNotFoundException.class, () -> client.getBoardGameByIdFromApi(boardGameId));
    }

    @Test
    public void getBoardGameByIdFromApi_shouldHandleJAXBException() throws Exception {
        Long boardGameId = 1L;
        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn("<invalid></xml>");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> client.getBoardGameByIdFromApi(boardGameId));
        assertTrue(exception.getMessage().contains("Failed to parse API response"));
    }

    @Test
    public void getExpansionByIdFromApi_shouldReturnExpansionDtos() throws JAXBException {
        String sampleXmlResponse = "<items><item><name type=\"primary\" value=\"Catan Expansion\"/></item></items>";
        List<Long> expansionIds = List.of(1L, 2L);

        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn(sampleXmlResponse);

        List<ExpansionDto> expansions = client.getExpansionByIdFromApi(expansionIds);

        assertNotNull(expansions);
        assertEquals(expansionIds.size()-1, expansions.size());
        verify(restTemplateMock, atLeastOnce()).getForObject(anyString(), eq(String.class));
    }

    @Test
    public void getExpansionByIdFromApi_shouldHandleEmptyResponse() {
        List<Long> expansionIds = List.of(1L, 2L);

        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn(null);

        List<ExpansionDto> expansions = client.getExpansionByIdFromApi(expansionIds);

        assertTrue(expansions.isEmpty());
        verify(restTemplateMock, atLeastOnce()).getForObject(anyString(), eq(String.class));
    }



    @Test
    public void getExpansionByIdFromApi_shouldHandleLargeInput() throws JAXBException {
        List<Long> expansionIds = new ArrayList<>();
        for (long i = 1; i <= 50; i++) {
            expansionIds.add(i);
        }

        String sampleXmlResponse = "<items><item><name type=\"primary\" value=\"Catan Expansion\"/></item></items>";
        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn(sampleXmlResponse);

        List<ExpansionDto> expansions = client.getExpansionByIdFromApi(expansionIds);

        assertNotNull(expansions);
        assertTrue(expansions.size() <= 50);
        verify(restTemplateMock, atLeast(3)).getForObject(anyString(), eq(String.class)); // Batch of 20
    }

    @Test
    void getBoardGameByIdFromApi_shouldThrowExceptionWhenItemsNull() throws JAXBException {
        String sampleXmlResponse = "<items></items>";
        Long boardGameId = 1L;

        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn(sampleXmlResponse);

        JAXBContext context = JAXBContext.newInstance(ItemsBoardGame.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(sampleXmlResponse);
        ItemsBoardGame items = (ItemsBoardGame) unmarshaller.unmarshal(reader);

        assertThrows(BoardGameNotFoundException.class, () -> {
            client.getBoardGameByIdFromApi(boardGameId);
        });

        verify(restTemplateMock, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    void getExpansionByIdFromApi_shouldLogErrorOnJAXBException() {
        List<Long> expansionIds = List.of(1L, 2L);
        String invalidXmlResponse = "<invalid></xml>";

        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn(invalidXmlResponse);

        List<ExpansionDto> result = client.getExpansionByIdFromApi(expansionIds);

        assertNotNull(result);
        assertTrue(result.isEmpty()); // Should return an empty list
        verify(restTemplateMock, atLeastOnce()).getForObject(anyString(), eq(String.class));
    }

    @Test
    void getExpansionByIdFromApi_shouldReturnEmptyForEmptyInputList() {
        List<Long> expansionIds = new ArrayList<>();

        List<ExpansionDto> result = client.getExpansionByIdFromApi(expansionIds);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(restTemplateMock, never()).getForObject(anyString(), eq(String.class));
    }

    @Test
    void getExpansionByIdFromApi_shouldHandleBatchProcessing() throws JAXBException {
        List<Long> expansionIds = new ArrayList<>();
        for (long i = 1; i <= 50; i++) {
            expansionIds.add(i);
        }

        String sampleXmlResponse = "<items><item><name type=\"primary\" value=\"Expansion\"/></item></items>";
        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn(sampleXmlResponse);

        List<ExpansionDto> result = client.getExpansionByIdFromApi(expansionIds);

        assertNotNull(result);
        verify(restTemplateMock, times(3)).getForObject(anyString(), eq(String.class));
    }

}