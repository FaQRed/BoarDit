package pl.sankouski.boarditupdater.mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto.Link;
import pl.sankouski.boarditdata.model.boardgame.Mechanic;
import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;
import pl.sankouski.boarditdata.repository.MechanicRepository;
import pl.sankouski.boarditupdater.boardGames.mapper.MechanicMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MechanicMapperTest {

    private MechanicMapper mechanicMapper;
    private BoarDitDataCatalog boarDitDataCatalog;
    private MechanicRepository mechanicRepository;

    @BeforeEach
    void setUp() {
        mechanicRepository = Mockito.mock(MechanicRepository.class);
        boarDitDataCatalog = Mockito.mock(BoarDitDataCatalog.class);
        when(boarDitDataCatalog.getMechanics()).thenReturn(mechanicRepository);

        mechanicMapper = new MechanicMapper();
    }

    @Test
    void mapMechanics_shouldMapNewMechanics() {
        BoardGameDto boardGameDto = createTestBoardGameDto();
        Mechanic mechanic = new Mechanic().setName("Test Mechanic").setBggId(123L);

        when(mechanicRepository.findByBggId(123L)).thenReturn(Optional.empty());
        when(mechanicRepository.save(any(Mechanic.class))).thenReturn(mechanic);

        List<Mechanic> result = mechanicMapper.mapMechanics(boardGameDto, boarDitDataCatalog);

        assertEquals(1, result.size());
        assertEquals("Test Mechanic", result.get(0).getName());
        assertEquals(123L, result.get(0).getBggId());
        verify(mechanicRepository, times(1)).save(any(Mechanic.class));
    }

    @Test
    void mapMechanics_shouldReturnExistingMechanics() {
        BoardGameDto boardGameDto = createTestBoardGameDto();
        Mechanic existingMechanic = new Mechanic().setName("Test Mechanic").setBggId(123L);

        when(mechanicRepository.findByBggId(123L)).thenReturn(Optional.of(existingMechanic));

        List<Mechanic> result = mechanicMapper.mapMechanics(boardGameDto, boarDitDataCatalog);

        assertEquals(1, result.size());
        assertEquals("Test Mechanic", result.get(0).getName());
        assertEquals(123L, result.get(0).getBggId());
        verify(mechanicRepository, never()).save(any(Mechanic.class));
    }

    private BoardGameDto createTestBoardGameDto() {
        Link mechanicLink = new Link("boardgamemechanic", "Test Mechanic", "123");
       BoardGameDto boardGameDto = new BoardGameDto();
       boardGameDto.setLinks(List.of(mechanicLink));
       return boardGameDto;
    }
}