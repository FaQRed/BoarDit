package pl.sankouski.boarditwebapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sankouski.boarditdata.dto.BoardGameDTO;
import pl.sankouski.boarditdata.dto.BoardGameUpdateSummaryDTO;
import pl.sankouski.boarditdata.model.boardgame.*;
import pl.sankouski.boarditdata.repository.*;
import pl.sankouski.boarditwebapi.service.boardgame.BoardGameService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoardGameServiceTest {

    @Mock
    private BoardGameRepository boardGameRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private AlternateNameRepository alternateNameRepository;

    @InjectMocks
    private BoardGameService boardGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllBoardGames() {
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        boardGame.setName("Test Game");
        when(boardGameRepository.findAll()).thenReturn(List.of(boardGame));

        List<BoardGameDTO> result = boardGameService.getAllBoardGames();

        assertEquals(1, result.size());
        assertEquals("Test Game", result.get(0).getName());
        verify(boardGameRepository, times(1)).findAll();
    }

    @Test
    void shouldGetBoardGameById() {
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        boardGame.setName("Test Game");
        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(boardGame));

        Optional<BoardGameDTO> result = boardGameService.getBoardGameById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Game", result.get().getName());
        verify(boardGameRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateBoardGame() {
        BoardGameDTO dto = new BoardGameDTO();
        dto.setName("New Game");
        dto.setYearPublished(2021);
        dto.setMinPlayers(2);
        dto.setMaxPlayers(4);
        dto.setPlayingTime(60);
        dto.setCategories(List.of(1L));
        dto.setMechanics(List.of(1L));

        BoardGame savedGame = new BoardGame();
        savedGame.setId(1L);
        savedGame.setName("New Game");
        when(boardGameRepository.findByName("New Game")).thenReturn(Optional.empty());
        when(categoryRepository.findAllById(dto.getCategories())).thenReturn(List.of(new Category()));
        when(mechanicRepository.findAllById(dto.getMechanics())).thenReturn(List.of(new Mechanic()));
        when(boardGameRepository.save(any(BoardGame.class))).thenReturn(savedGame);

        BoardGameDTO result = boardGameService.createBoardGame(dto);

        assertNotNull(result);
        assertEquals("New Game", result.getName());
        verify(boardGameRepository, times(1)).findByName("New Game");
        verify(boardGameRepository, times(2)).save(any(BoardGame.class));
    }

    @Test
    void shouldUpdateBoardGame() {
        BoardGameUpdateSummaryDTO dto = new BoardGameUpdateSummaryDTO();
        dto.setName("Updated Game");
        dto.setYearPublished(2022);
        dto.setMinPlayers(2);
        dto.setMaxPlayers(5);
        dto.setPlayingTime(90);

        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        boardGame.setName("Old Game");
        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(boardGame));
        when(boardGameRepository.findByName("Updated Game")).thenReturn(Optional.empty());
        when(boardGameRepository.save(any(BoardGame.class))).thenReturn(boardGame);

        BoardGameDTO result = boardGameService.updateBoardGame(1L, dto);

        assertNotNull(result);
        assertEquals("Updated Game", result.getName());
        verify(boardGameRepository, times(1)).findById(1L);
        verify(boardGameRepository, times(1)).save(boardGame);
    }

    @Test
    void shouldDeleteBoardGame() {
        when(boardGameRepository.existsById(1L)).thenReturn(true);

        boardGameService.deleteBoardGame(1L);

        verify(boardGameRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldAddCategoryToBoardGame() {
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        boardGame.setCategories(new ArrayList<>());

        Category category = new Category();
        category.setId(1L);

        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(boardGame));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        boardGameService.addCategoryToBoardGame(1L, 1L);

        assertTrue(boardGame.getCategories().contains(category));
        verify(boardGameRepository, times(1)).save(boardGame);
    }

    @Test
    void shouldRemoveCategoryFromBoardGame() {
        Category category = new Category();
        category.setId(1L);

        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        boardGame.setCategories(new ArrayList<>(List.of(category)));

        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(boardGame));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        boardGameService.removeCategoryFromBoardGame(1L, 1L);

        assertFalse(boardGame.getCategories().contains(category));
        verify(boardGameRepository, times(1)).save(boardGame);
    }

    @Test
    void shouldAddMechanicToBoardGame() {
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        boardGame.setMechanics(new ArrayList<>());

        Mechanic mechanic = new Mechanic();
        mechanic.setId(1L);

        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(boardGame));
        when(mechanicRepository.findById(1L)).thenReturn(Optional.of(mechanic));

        boardGameService.addMechanicToBoardGame(1L, 1L);

        assertTrue(boardGame.getMechanics().contains(mechanic));
        verify(boardGameRepository, times(1)).save(boardGame);
    }

    @Test
    void shouldRemoveMechanicFromBoardGame() {
        Mechanic mechanic = new Mechanic();
        mechanic.setId(1L);

        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        boardGame.setMechanics(new ArrayList<>(List.of(mechanic)));

        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(boardGame));
        when(mechanicRepository.findById(1L)).thenReturn(Optional.of(mechanic));

        boardGameService.removeMechanicFromBoardGame(1L, 1L);

        assertFalse(boardGame.getMechanics().contains(mechanic));
        verify(boardGameRepository, times(1)).save(boardGame);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentBoardGame() {
        when(boardGameRepository.existsById(999L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardGameService.deleteBoardGame(999L)
        );

        assertEquals("Board game with ID 999 not found.", exception.getMessage());
        verify(boardGameRepository, times(1)).existsById(999L);
        verify(boardGameRepository, never()).deleteById(999L);
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicateCategoryToBoardGame() {
        Category category = new Category();
        category.setId(1L);

        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        boardGame.setCategories(new ArrayList<>(List.of(category)));

        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(boardGame));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardGameService.addCategoryToBoardGame(1L, 1L)
        );

        assertEquals("Category already associated with this BoardGame.", exception.getMessage());
        verify(boardGameRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(1L);
        verify(boardGameRepository, never()).save(any(BoardGame.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingInvalidMechanicToBoardGame() {
        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(new BoardGame()));
        when(mechanicRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardGameService.addMechanicToBoardGame(1L, 999L)
        );

        assertEquals("Mechanic not found", exception.getMessage());
        verify(boardGameRepository, times(1)).findById(1L);
        verify(mechanicRepository, times(1)).findById(999L);
        verify(boardGameRepository, never()).save(any(BoardGame.class));
    }

    @Test
    void shouldValidateBoardGameDTOWithInvalidPlayersRange() {
        BoardGameDTO dto = new BoardGameDTO();
        dto.setName("Invalid Game");
        dto.setYearPublished(2021);
        dto.setMinPlayers(5);
        dto.setMaxPlayers(4);
        dto.setCategories(List.of(1L));
        dto.setMechanics(List.of(1L));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardGameService.createBoardGame(dto)
        );

        assertEquals(
                "Invalid player range. Ensure that minPlayers > 0, maxPlayers > 0, and minPlayers <= maxPlayers.",
                exception.getMessage()
        );
        verify(boardGameRepository, never()).save(any(BoardGame.class));
    }

    @Test
    void shouldCreateBoardGameWithArtists() {
        BoardGameDTO dto = new BoardGameDTO();
        dto.setName("New Game");
        dto.setYearPublished(2022);
        dto.setMinPlayers(2);
        dto.setMaxPlayers(4);
        dto.setPlayingTime(60);
        dto.setArtists(List.of("Artist 1", "Artist 2"));
        dto.setCategories(List.of(1L));
        dto.setMechanics(List.of(1L));

        BoardGame savedGame = new BoardGame();
        savedGame.setId(1L);
        savedGame.setName("New Game");

        Artist existingArtist = new Artist();
        existingArtist.setId(1L);
        existingArtist.setName("Artist 1");

        Artist newArtist = new Artist();
        newArtist.setId(2L);
        newArtist.setName("Artist 2");

        when(boardGameRepository.findByName("New Game")).thenReturn(Optional.empty());
        when(categoryRepository.findAllById(dto.getCategories())).thenReturn(List.of(new Category()));
        when(mechanicRepository.findAllById(dto.getMechanics())).thenReturn(List.of(new Mechanic()));
        when(boardGameRepository.save(any(BoardGame.class))).thenReturn(savedGame);
        when(artistRepository.findByName("Artist 1")).thenReturn(Optional.of(existingArtist));
        when(artistRepository.findByName("Artist 2")).thenReturn(Optional.empty());
        when(artistRepository.save(any(Artist.class))).thenReturn(newArtist);

        BoardGameDTO result = boardGameService.createBoardGame(dto);

        assertNotNull(result);
        assertEquals("New Game", result.getName());
        verify(artistRepository, times(1)).findByName("Artist 1");
        verify(artistRepository, times(1)).findByName("Artist 2");
        verify(artistRepository, times(1)).save(any(Artist.class));
        verify(boardGameRepository, times(2)).save(any(BoardGame.class));
    }

    @Test
    void shouldCreateBoardGameWithAlternateNames() {
        BoardGameDTO dto = new BoardGameDTO();
        dto.setName("New Game");
        dto.setYearPublished(2022);
        dto.setMinPlayers(2);
        dto.setMaxPlayers(4);
        dto.setPlayingTime(60);
        dto.setAlternateNames(List.of("Alt Name 1", "Alt Name 2"));
        dto.setCategories(List.of(1L));
        dto.setMechanics(List.of(1L));

        BoardGame savedGame = new BoardGame();
        savedGame.setId(1L);
        savedGame.setName("New Game");

        AlternateName existingAltName = new AlternateName();
        existingAltName.setId(1L);
        existingAltName.setName("Alt Name 1");

        AlternateName newAltName = new AlternateName();
        newAltName.setId(2L);
        newAltName.setName("Alt Name 2");

        when(boardGameRepository.findByName("New Game")).thenReturn(Optional.empty());
        when(categoryRepository.findAllById(dto.getCategories())).thenReturn(List.of(new Category()));
        when(mechanicRepository.findAllById(dto.getMechanics())).thenReturn(List.of(new Mechanic()));
        when(boardGameRepository.save(any(BoardGame.class))).thenReturn(savedGame);
        when(alternateNameRepository.findByName("Alt Name 1")).thenReturn(Optional.of(existingAltName));
        when(alternateNameRepository.findByName("Alt Name 2")).thenReturn(Optional.empty());
        when(alternateNameRepository.save(any(AlternateName.class))).thenReturn(newAltName);

        BoardGameDTO result = boardGameService.createBoardGame(dto);


        assertNotNull(result);
        assertEquals("New Game", result.getName());
        verify(alternateNameRepository, times(1)).findByName("Alt Name 1");
        verify(alternateNameRepository, times(1)).findByName("Alt Name 2");
        verify(alternateNameRepository, times(1)).save(any(AlternateName.class));
        verify(boardGameRepository, times(2)).save(any(BoardGame.class));
    }
    @Test
    void validateBoardGameUpdateSummaryDTO_shouldThrowExceptionForInvalidInput() {
        BoardGameUpdateSummaryDTO dto = new BoardGameUpdateSummaryDTO();


        dto.setName("");
        dto.setYearPublished(2020);
        dto.setMinPlayers(2);
        dto.setMaxPlayers(4);
        dto.setPlayingTime(60);
        dto.setImageLink("http://validlink.com/image.jpg");
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameUpdateSummaryDTO(dto),
                "Expected exception for empty name"
        );


        dto.setName("Valid Name");
        dto.setYearPublished(-1);
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameUpdateSummaryDTO(dto),
                "Expected exception for invalid year published"
        );


        dto.setYearPublished(2020);
        dto.setMinPlayers(5);
        dto.setMaxPlayers(4);
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameUpdateSummaryDTO(dto),
                "Expected exception for min players > max players"
        );

        dto.setMinPlayers(2);
        dto.setMaxPlayers(4);
        dto.setPlayingTime(0);
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameUpdateSummaryDTO(dto),
                "Expected exception for invalid playing time"
        );

        dto.setPlayingTime(60);
        dto.setImageLink("invalid-url");
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameUpdateSummaryDTO(dto),
                "Expected exception for invalid image link"
        );
    }

    @Test
    void validateBoardGameUpdateSummaryDTO_shouldPassForValidInput() {
        BoardGameUpdateSummaryDTO dto = new BoardGameUpdateSummaryDTO();
        dto.setName("Valid Name");
        dto.setYearPublished(2020);
        dto.setMinPlayers(2);
        dto.setMaxPlayers(4);
        dto.setPlayingTime(60);
        dto.setImageLink("http://validlink.com/image.jpg");

        assertDoesNotThrow(() ->
                        boardGameService.validateBoardGameUpdateSummaryDTO(dto),
                "No exception should be thrown for valid input"
        );
    }

    @Test
    void validateBoardGameDTO_shouldThrowExceptionForInvalidInput() {
        BoardGameDTO dto = new BoardGameDTO();

        dto.setName(null);
        dto.setYearPublished(2020);
        dto.setMinPlayers(2);
        dto.setMaxPlayers(4);
        dto.setCategories(List.of(1L));
        dto.setMechanics(List.of(1L));
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameDTO(dto),
                "Expected exception for null name"
        );

        dto.setName("  ");
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameDTO(dto),
                "Expected exception for blank name"
        );

        dto.setName("Valid Name");
        dto.setYearPublished(0);
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameDTO(dto),
                "Expected exception for invalid year published"
        );

        dto.setYearPublished(2020);
        dto.setMinPlayers(0);
        dto.setMaxPlayers(4);
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameDTO(dto),
                "Expected exception for minPlayers <= 0"
        );

        dto.setMinPlayers(5);
        dto.setMaxPlayers(4);
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameDTO(dto),
                "Expected exception for minPlayers > maxPlayers"
        );

        dto.setMinPlayers(2);
        dto.setMaxPlayers(4);
        dto.setCategories(null);
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameDTO(dto),
                "Expected exception for null categories"
        );

        dto.setCategories(List.of());
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameDTO(dto),
                "Expected exception for empty categories"
        );

        dto.setCategories(List.of(1L));
        dto.setMechanics(null);
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameDTO(dto),
                "Expected exception for null mechanics"
        );

        dto.setMechanics(List.of());
        assertThrows(IllegalArgumentException.class, () ->
                        boardGameService.validateBoardGameDTO(dto),
                "Expected exception for empty mechanics"
        );
    }

    @Test
    void validateBoardGameDTO_shouldPassForValidInput() {
        BoardGameDTO dto = new BoardGameDTO();
        dto.setName("Valid Name");
        dto.setYearPublished(2020);
        dto.setMinPlayers(2);
        dto.setMaxPlayers(4);
        dto.setCategories(List.of(1L));
        dto.setMechanics(List.of(1L));

        assertDoesNotThrow(() ->
                        boardGameService.validateBoardGameDTO(dto),
                "No exception should be thrown for valid input"
        );
    }
}