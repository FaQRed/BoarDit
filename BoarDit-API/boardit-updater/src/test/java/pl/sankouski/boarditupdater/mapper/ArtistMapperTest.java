package pl.sankouski.boarditupdater.mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto.Link;
import pl.sankouski.boarditdata.model.boardgame.Artist;
import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;
import pl.sankouski.boarditdata.repository.ArtistRepository;
import pl.sankouski.boarditupdater.boardGames.mapper.ArtistMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ArtistMapperTest {

    @Mock
    private BoarDitDataCatalog boarDitDataCatalog;

    @Mock
    private ArtistRepository artistRepository;

    private ArtistMapper artistMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        artistMapper = new ArtistMapper();
        when(boarDitDataCatalog.getArtists()).thenReturn(artistRepository);
    }

    @Test
    public void mapArtist_shouldSaveAndReturnNewArtist() {
        BoardGameDto boardGameDto = new BoardGameDto();
        boardGameDto.setLinks(List.of(new Link("boardgameartist", "John Doe", "123")));

        when(artistRepository.findByBggId(123L)).thenReturn(Optional.empty());

        List<Artist> result = artistMapper.mapArtist(boardGameDto, boarDitDataCatalog);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals(123L, result.get(0).getBggId());

        verify(artistRepository, times(1)).save(any(Artist.class));
    }

    @Test
    public void mapArtist_shouldReturnExistingArtist() {
        BoardGameDto boardGameDto = new BoardGameDto();
        boardGameDto.setLinks(List.of(new Link("boardgameartist", "John Doe", "123")));

        Artist existingArtist = new Artist().setName("John Doe").setBggId(123L);
        when(artistRepository.findByBggId(123L)).thenReturn(Optional.of(existingArtist));

        List<Artist> result = artistMapper.mapArtist(boardGameDto, boarDitDataCatalog);

        assertEquals(1, result.size());
        assertEquals(existingArtist, result.get(0));

        verify(artistRepository, never()).save(any(Artist.class));
    }

    @Test
    public void mapArtist_shouldIgnoreNonArtistLinks() {
        BoardGameDto boardGameDto = new BoardGameDto();
        boardGameDto.setLinks(List.of(new Link("boardgamecategory", "Category Name", "456")));

        List<Artist> result = artistMapper.mapArtist(boardGameDto, boarDitDataCatalog);

        assertEquals(0, result.size());
        verify(artistRepository, never()).findByBggId(anyLong());
        verify(artistRepository, never()).save(any(Artist.class));
    }
}