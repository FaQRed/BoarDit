package pl.sankouski.boarditupdater;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.sankouski.boarditboardgamesclient.BoardGameClient;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditupdater.boardGames.mapper.BoardGameMapper;
import pl.sankouski.boarditupdater.boardGames.updater.BoardGamesUpdater;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BoardGameUpdaterTest {

    private BoarDitDataCatalog dataCatalog;
    private BoardGameClient boardGameClient;
    private BoardGameMapper boardGameMapper;
    private BoardGamesUpdater boardGamesUpdater;
    private BoardGameRepository boardGameRepository;

    @BeforeEach
    void setUp() {
        boardGameClient = Mockito.mock(BoardGameClient.class);
        boardGameMapper = Mockito.mock(BoardGameMapper.class);
        dataCatalog = Mockito.mock(BoarDitDataCatalog.class);
        boardGameRepository = Mockito.mock(BoardGameRepository.class);

        when(dataCatalog.getBoardGame()).thenReturn(boardGameRepository);

        boardGamesUpdater = new BoardGamesUpdater(dataCatalog, boardGameClient, boardGameMapper);
    }

    @Test
    void updateByIdFromBgg_shouldUpdateBoardGame() {
        BoardGameDto boardGameDto = createTestBoardGameDto();
        BoardGame boardGame = createTestBoardGame();

        when(boardGameClient.getBoardGameByIdFromApi(1L)).thenReturn(boardGameDto);
        when(boardGameMapper.toEntity(boardGameDto, dataCatalog)).thenReturn(boardGame);

        boardGamesUpdater.updateByIdFromBgg(1);

        verify(boardGameClient, times(1)).getBoardGameByIdFromApi(1L);
        verify(boardGameMapper, times(1)).toEntity(boardGameDto, dataCatalog);
        verify(boardGameRepository, times(1)).save(boardGame);
    }

    @Test
    void updateByIdFromBgg_shouldHandleNullDto() {
        when(boardGameClient.getBoardGameByIdFromApi(1L)).thenReturn(null);

        boardGamesUpdater.updateByIdFromBgg(1);

        verify(boardGameClient, times(1)).getBoardGameByIdFromApi(1L);
        verify(boardGameMapper, never()).toEntity(any(), any());
        verify(boardGameRepository, never()).save(any());
    }

    private BoardGameDto createTestBoardGameDto() {

        BoardGameDto boardGameDto = new BoardGameDto();
        boardGameDto
                .setBggId(1L)
                .setNames(List.of(new BoardGameDto.Name("primary", "Test Game")));
        boardGameDto
                .setDescription("Test Description");
        return boardGameDto;
    }

    private BoardGame createTestBoardGame() {
        return new BoardGame()
                .setId(1L)
                .setName("Test Game")
                .setDescription("Test Description");
    }
}