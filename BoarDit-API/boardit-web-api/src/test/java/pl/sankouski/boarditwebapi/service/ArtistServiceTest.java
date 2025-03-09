package pl.sankouski.boarditwebapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sankouski.boarditdata.dto.ArtistDTO;
import pl.sankouski.boarditdata.model.boardgame.Artist;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.repository.ArtistRepository;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditwebapi.service.boardgame.ArtistService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArtistServiceTest {

    @InjectMocks
    private ArtistService artistService;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private BoardGameRepository boardGameRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldConvertArtistToDTO() {
        Artist artist = new Artist();
        artist.setId(1L);
        artist.setName("Test Artist");

        ArtistDTO dto = artistService.convertToDTO(artist);

        assertEquals(1L, dto.getId());
        assertEquals("Test Artist", dto.getName());
    }

    @Test
    void shouldConvertDTOToArtist() {
        ArtistDTO dto = new ArtistDTO(1L, "Test Artist");

        Artist artist = artistService.convertFromDTO(dto);

        assertEquals(1L, artist.getId());
        assertEquals("Test Artist", artist.getName());
    }

    @Test
    void shouldReturnAllArtists() {
        Artist artist = new Artist();
        artist.setId(1L);
        artist.setName("Artist Name");

        when(artistRepository.findAll()).thenReturn(List.of(artist));

        List<ArtistDTO> result = artistService.getAllArtists();

        assertEquals(1, result.size());
        assertEquals("Artist Name", result.get(0).getName());
    }

    @Test
    void shouldReturnArtistById() {
        Artist artist = new Artist();
        artist.setId(1L);
        artist.setName("Artist Name");

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        Optional<ArtistDTO> result = artistService.getArtistById(1L);

        assertTrue(result.isPresent());
        assertEquals("Artist Name", result.get().getName());
    }

    @Test
    void shouldCreateArtist() {
        ArtistDTO dto = new ArtistDTO(null, "New Artist");
        Artist artist = new Artist();
        artist.setName("New Artist");

        when(artistRepository.existsByName("New Artist")).thenReturn(false);
        when(artistRepository.save(any(Artist.class))).thenReturn(artist);

        ArtistDTO result = artistService.createArtist(dto);

        assertEquals("New Artist", result.getName());
        verify(artistRepository).save(any(Artist.class));
    }

    @Test
    void shouldNotCreateDuplicateArtist() {
        ArtistDTO dto = new ArtistDTO(null, "Duplicate Artist");

        when(artistRepository.existsByName("Duplicate Artist")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> artistService.createArtist(dto));

        assertEquals("Artist with name 'Duplicate Artist' already exists.", exception.getMessage());
    }

    @Test
    void shouldUpdateArtist() {
        Artist existingArtist = new Artist();
        existingArtist.setId(1L);
        existingArtist.setName("Old Artist");

        ArtistDTO dto = new ArtistDTO(1L, "Updated Artist");

        when(artistRepository.findById(1L)).thenReturn(Optional.of(existingArtist));
        when(artistRepository.existsByName("Updated Artist")).thenReturn(false);
        when(artistRepository.save(existingArtist)).thenReturn(existingArtist);

        ArtistDTO result = artistService.updateArtist(1L, dto);

        assertEquals("Updated Artist", result.getName());
        verify(artistRepository).save(existingArtist);
    }

    @Test
    void shouldNotUpdateToExistingArtistName() {
        Artist existingArtist = new Artist();
        existingArtist.setId(1L);
        existingArtist.setName("Old Artist");

        ArtistDTO dto = new ArtistDTO(1L, "Existing Artist");

        when(artistRepository.findById(1L)).thenReturn(Optional.of(existingArtist));
        when(artistRepository.existsByName("Existing Artist")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> artistService.updateArtist(1L, dto));

        assertEquals("Artist with name 'Existing Artist' already exists.", exception.getMessage());
    }

    @Test
    void shouldDeleteArtist() {
        Artist artist = new Artist();
        artist.setId(1L);
        artist.setName("Artist");
        artist.setBoardGames(Collections.emptyList());

        when(artistRepository.existsById(1L)).thenReturn(true);
        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        artistService.deleteArtist(1L);

        verify(artistRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentArtist() {
        when(artistRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> artistService.deleteArtist(1L));

        assertEquals("Artist with ID 1 not found.", exception.getMessage());
    }

    @Test
    void shouldCreateArtistForBoardGame() {
        ArtistDTO artistDTO = new ArtistDTO(null, "New Artist");
        Artist artist = new Artist();
        artist.setName("New Artist");

        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        boardGame.setArtists(new ArrayList<>());
        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(boardGame));
        when(artistRepository.save(any(Artist.class))).thenReturn(artist);
        artistService.createArtistForBoardGame(1L, artistDTO);
        verify(artistRepository).save(any(Artist.class));
        verify(boardGameRepository).save(boardGame);
        assertEquals(1, boardGame.getArtists().size());
        assertEquals("New Artist", boardGame.getArtists().get(0).getName());
    }

    @Test
    void shouldThrowIfArtistAlreadyExistsForBoardGame() {
        ArtistDTO artistDTO = new ArtistDTO(null, "Existing Artist");
        Artist artist = new Artist();
        artist.setName("Existing Artist");
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        boardGame.setArtists(List.of(artist));

        when(boardGameRepository.findById(1L)).thenReturn(Optional.of(boardGame));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> artistService.createArtistForBoardGame(1L, artistDTO));

        assertEquals("Artist with the same name already exists for this board game.", exception.getMessage());
    }
}