package pl.sankouski.boarditwebapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sankouski.boarditdata.dto.ExpansionDTO;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.model.boardgame.Expansion;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditdata.repository.ExpansionRepository;
import pl.sankouski.boarditwebapi.service.boardgame.ExpansionService;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpansionServiceTest {

    @Mock
    private ExpansionRepository expansionRepository;

    @Mock
    private BoardGameRepository boardGameRepository;

    @InjectMocks
    private ExpansionService expansionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllExpansions() {
        Expansion expansion = new Expansion();
        expansion.setId(1L);
        expansion.setName("Expansion 1");
        when(expansionRepository.findAll()).thenReturn(List.of(expansion));

        List<ExpansionDTO> result = expansionService.getAllExpansions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Expansion 1", result.get(0).getName());
        verify(expansionRepository, times(1)).findAll();
    }

    @Test
    void shouldGetExpansionById() {
        Expansion expansion = new Expansion();
        expansion.setId(1L);
        expansion.setName("Expansion 1");
        when(expansionRepository.findById(1L)).thenReturn(Optional.of(expansion));

        ExpansionDTO result = expansionService.getExpansionById(1L);

        assertNotNull(result);
        assertEquals("Expansion 1", result.getName());
        verify(expansionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateExpansion() {
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(boardGame));

        ExpansionDTO expansionDTO = new ExpansionDTO()
                .setName("New Expansion")
                .setDescription("Description")
                .setYearPublished(2022)
                .setMinPlayers(2)
                .setMaxPlayers(4)
                .setPlayingTime(60);

        Expansion expansion = new Expansion()
                .setId(1L)
                .setName("New Expansion");
        when(expansionRepository.save(any(Expansion.class))).thenReturn(expansion);

        ExpansionDTO result = expansionService.createExpansion(1L, expansionDTO);

        assertNotNull(result);
        assertEquals("New Expansion", result.getName());
        verify(boardGameRepository, times(1)).findById(1L);
        verify(expansionRepository, times(1)).save(any(Expansion.class));
    }

    @Test
    void shouldUpdateExpansion() {
        Expansion existingExpansion = new Expansion()
                .setId(1L)
                .setName("Old Expansion");
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);

        when(expansionRepository.findById(1L)).thenReturn(Optional.of(existingExpansion));
        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(boardGame));
        when(expansionRepository.save(any(Expansion.class))).thenReturn(existingExpansion);

        ExpansionDTO updateDTO = new ExpansionDTO()
                .setId(1L)
                .setName("Updated Expansion")
                .setDescription("Updated Description")
                .setYearPublished(2023)
                .setMinPlayers(2)
                .setMaxPlayers(6)
                .setPlayingTime(90);

        ExpansionDTO result = expansionService.updateExpansion(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Expansion", result.getName());
        verify(expansionRepository, times(1)).findById(1L);
        verify(boardGameRepository, times(1)).findById(1L);
        verify(expansionRepository, times(1)).save(existingExpansion);
    }

    @Test
    void shouldDeleteExpansion() {
        Expansion expansion = new Expansion();
        expansion.setId(1L);
        when(expansionRepository.findById(1L)).thenReturn(Optional.of(expansion));

        expansionService.deleteExpansion(1L);

        verify(expansionRepository, times(1)).findById(1L);
        verify(expansionRepository, times(1)).delete(expansion);
    }

    @Test
    void shouldGetAllExpansionsByBoardGameId() {
        ExpansionDTO expansionDTO = new ExpansionDTO()
                .setId(1L)
                .setName("Expansion 1");
        when(expansionRepository.getExpansionsByBoardGame_Id(1L)).thenReturn(List.of(expansionDTO));

        List<ExpansionDTO> result = expansionService.getAllExpansionsByBoardGameId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Expansion 1", result.get(0).getName());
        verify(expansionRepository, times(1)).getExpansionsByBoardGame_Id(1L);
    }

    @Test
    void shouldThrowExceptionWhenCreatingExpansionWithInvalidYear() {
        ExpansionDTO expansionDTO = new ExpansionDTO()
                .setName("Invalid Year Expansion")
                .setDescription("Description")
                .setYearPublished(1800) // Invalid year
                .setMinPlayers(2)
                .setMaxPlayers(4)
                .setPlayingTime(60);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> expansionService.createExpansion(1L, expansionDTO)
        );

        assertEquals("Year Published must be between 1900 and " + Year.now().getValue(), exception.getMessage());
        verify(boardGameRepository, never()).findById(anyLong());
        verify(expansionRepository, never()).save(any(Expansion.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingExpansionWithNegativePlayingTime() {
        ExpansionDTO expansionDTO = new ExpansionDTO()
                .setName("Invalid Playing Time Expansion")
                .setDescription("Description")
                .setYearPublished(2022)
                .setMinPlayers(2)
                .setMaxPlayers(4)
                .setPlayingTime(-10); // Invalid playing time

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> expansionService.createExpansion(1L, expansionDTO)
        );

        assertEquals("Playing time cannot be negative.", exception.getMessage());
        verify(boardGameRepository, never()).findById(anyLong());
        verify(expansionRepository, never()).save(any(Expansion.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingExpansionWithEmptyName() {
        ExpansionDTO expansionDTO = new ExpansionDTO()
                .setName("") // Empty name
                .setDescription("Description")
                .setYearPublished(2022)
                .setMinPlayers(2)
                .setMaxPlayers(4)
                .setPlayingTime(60);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> expansionService.createExpansion(1L, expansionDTO)
        );

        assertEquals("Expansion name cannot be empty.", exception.getMessage());
        verify(boardGameRepository, never()).findById(anyLong());
        verify(expansionRepository, never()).save(any(Expansion.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingExpansionWithInvalidPlayersRange() {
        ExpansionDTO updateDTO = new ExpansionDTO()
                .setId(1L)
                .setName("Expansion with Invalid Players Range")
                .setDescription("Description")
                .setYearPublished(2022)
                .setMinPlayers(5) // Min players > Max players
                .setMaxPlayers(3)
                .setPlayingTime(60);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> expansionService.updateExpansion(1L, updateDTO)
        );

        assertEquals("Maximum number of players cannot be less than minimum players.", exception.getMessage());
        verify(expansionRepository, never()).save(any(Expansion.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentExpansion() {
        when(expansionRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> expansionService.deleteExpansion(999L)
        );

        assertEquals("Expansion with ID 999 not found.", exception.getMessage());
        verify(expansionRepository, times(1)).findById(999L);
        verify(expansionRepository, never()).delete(any(Expansion.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentExpansion() {
        ExpansionDTO updateDTO = new ExpansionDTO()
                .setId(999L)
                .setName("Non-existent Expansion")
                .setDescription("Description")
                .setYearPublished(2022)
                .setMinPlayers(2)
                .setMaxPlayers(4)
                .setPlayingTime(60);

        when(expansionRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> expansionService.updateExpansion(1L, updateDTO)
        );

        assertEquals("Expansion with ID 999 not found.", exception.getMessage());
        verify(expansionRepository, times(1)).findById(999L);
        verify(expansionRepository, never()).save(any(Expansion.class));
    }
}