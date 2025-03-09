package pl.sankouski.boarditupdater.mapper;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sankouski.boarditboardgamesclient.BoardGameClient;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditboardgamesclient.dto.ExpansionDto;
import pl.sankouski.boarditboardgamesclient.exception.GameAlreadyExistsException;
import pl.sankouski.boarditdata.model.boardgame.*;
import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditupdater.boardGames.mapper.ArtistMapper;
import pl.sankouski.boarditupdater.boardGames.mapper.BoardGameMapper;
import pl.sankouski.boarditupdater.boardGames.mapper.CategoryMapper;
import pl.sankouski.boarditupdater.boardGames.mapper.MechanicMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardGameMapperTest {

    @Mock
    private BoardGameClient boardGameClient;

    @Mock
    private MechanicMapper mechanicMapper;

    @Mock
    private ArtistMapper artistMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BoardGameRepository boardGameRepository;

    @Mock
    private BoarDitDataCatalog boarDitDataCatalog;

    private BoardGameMapper boardGameMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        boardGameMapper = new BoardGameMapper(boardGameClient, mechanicMapper, artistMapper, categoryMapper);
        when(boarDitDataCatalog.getBoardGame()).thenReturn(boardGameRepository);
    }

    @Test
    public void toEntity_shouldMapDtoToEntity() {
        BoardGameDto dto = createTestBoardGameDto();

        when(boarDitDataCatalog.getBoardGame().findByBggId(dto.getBggId())).thenReturn(Optional.empty());
        when(boardGameClient.getExpansionByIdFromApi(List.of(456L))).thenReturn(List.of(createTestExpansionDto()));
        when(mechanicMapper.mapMechanics(dto, boarDitDataCatalog)).thenReturn(List.of(new Mechanic().setName("Mechanic 1")));
        when(artistMapper.mapArtist(dto, boarDitDataCatalog)).thenReturn(List.of(new Artist().setName("Artist 1")));
        when(categoryMapper.mapCategory(dto, boarDitDataCatalog)).thenReturn(List.of(new Category().setName("Category 1")));

        BoardGame boardGame = boardGameMapper.toEntity(dto, boarDitDataCatalog);

        assertEquals("Test Game", boardGame.getName());
        assertEquals("Test Description", boardGame.getDescription());
        assertEquals(2022, boardGame.getYearPublished());
        assertEquals(2, boardGame.getMinPlayers());
        assertEquals(4, boardGame.getMaxPlayers());
        assertEquals(60, boardGame.getPlayingTime());
        assertEquals("http://example.com/image.jpg", boardGame.getImageLink());
        assertEquals(123L, boardGame.getBggId());

        assertEquals(1, boardGame.getAlternateNames().size());
        assertEquals("Alternate Name", boardGame.getAlternateNames().get(0).getName());

        assertEquals(1, boardGame.getExpansions().size());
        assertEquals("Expansion 1", boardGame.getExpansions().get(0).getName());

        assertEquals(1, boardGame.getMechanics().size());
        assertEquals("Mechanic 1", boardGame.getMechanics().get(0).getName());

        assertEquals(1, boardGame.getArtists().size());
        assertEquals("Artist 1", boardGame.getArtists().get(0).getName());

        assertEquals(1, boardGame.getCategories().size());
        assertEquals("Category 1", boardGame.getCategories().get(0).getName());
    }

    @Test
    public void toEntity_shouldThrowGameAlreadyExistsException() {
        BoardGameDto dto = createTestBoardGameDto();

        when(boarDitDataCatalog.getBoardGame().findByBggId(dto.getBggId())).thenReturn(Optional.of(new BoardGame()));

        assertThrows(GameAlreadyExistsException.class, () -> boardGameMapper.toEntity(dto, boarDitDataCatalog));
    }

    private BoardGameDto createTestBoardGameDto() {
        BoardGameDto boardGameDto = new  BoardGameDto();
        boardGameDto
                .setBggId(123L)
                .setNames(List.of(new BoardGameDto.Name("primary", "Test Game"),
                        new BoardGameDto.Name("alternate", "Alternate Name")));
        boardGameDto
                .setDescription("Test Description");
        boardGameDto
                .setYearPublished(new BoardGameDto.YearPublished() {{
                    setValue(2022);
                }})
                .setMinPlayers(new BoardGameDto.MinPlayers() {{
                    setValue(2);
                }})
                .setMaxPlayers(new BoardGameDto.MaxPlayers() {{
                    setValue(4);
                }})
                .setPlayingTime(new BoardGameDto.PlayingTime() {{
                    setValue(60);
                }})
                .setImageLink("http://example.com/image.jpg");
        boardGameDto
                .setLinks(List.of(new BoardGameDto.Link("boardgameexpansion", "Expansion 1", "456")));
        return boardGameDto;
    }

    private ExpansionDto createTestExpansionDto() {
        return new ExpansionDto()
                .setBggId(456L)
                .setNames(List.of(new ExpansionDto.Name("primary", "Expansion 1")))
                .setDescription("Expansion Description")
                .setYearPublished(new ExpansionDto.YearPublished() {{
                    setValue(2023);
                }})
                .setMinPlayers(new ExpansionDto.MinPlayers() {{
                    setValue(2);
                }})
                .setMaxPlayers(new ExpansionDto.MaxPlayers() {{
                    setValue(5);
                }})
                .setPlayingTime(new ExpansionDto.PlayingTime() {{
                    setValue(90);
                }})
                .setImageLink("http://example.com/expansion.jpg");
    }
}