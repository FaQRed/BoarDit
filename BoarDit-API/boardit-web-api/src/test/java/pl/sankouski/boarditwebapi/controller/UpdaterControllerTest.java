package pl.sankouski.boarditwebapi.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditupdater.boardGames.updater.IUpdateBoardGames;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UpdaterControllerTest {

    @Mock
    private IUpdateBoardGames updater;

    @Mock
    private BoarDitDataCatalog catalog;

    @InjectMocks
    private UpdaterController updaterController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(updaterController).build();
        BoardGameRepository mockBoardGameRepository = mock(BoardGameRepository.class);
        when(catalog.getBoardGame()).thenReturn(mockBoardGameRepository);
    }

    @Test
    void shouldStartUpdateAndReturnSuccessMessage() throws Exception {
        int testNumber = 12345;
        BoardGame mockBoardGame = new BoardGame();
        mockBoardGame.setName("Test Board Game");

        BoardGameRepository mockBoardGameRepository = catalog.getBoardGame();
        when(mockBoardGameRepository.findByBggId((long) testNumber)).thenReturn(Optional.of(mockBoardGame));

        mockMvc.perform(get("/admin/updater/start")
                        .param("number", String.valueOf(testNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Was added/updated Test Board Game game"));

        verify(updater, times(1)).updateByIdFromBgg(testNumber);
        verify(mockBoardGameRepository, times(1)).findByBggId((long) testNumber);
    }

    @Test
    void shouldReturnErrorMessageWhenGameNotFound() throws Exception {
        int testNumber = 12345;
        BoardGameRepository mockBoardGameRepository = catalog.getBoardGame();
        when(mockBoardGameRepository.findByBggId((long) testNumber)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/updater/start")
                        .param("number", String.valueOf(testNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No game found with BGG ID: " + testNumber));

        verify(updater, times(1)).updateByIdFromBgg(testNumber);
        verify(mockBoardGameRepository, times(1)).findByBggId((long) testNumber);
    }
}