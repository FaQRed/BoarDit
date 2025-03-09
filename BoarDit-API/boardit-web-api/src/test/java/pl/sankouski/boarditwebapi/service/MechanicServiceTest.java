package pl.sankouski.boarditwebapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sankouski.boarditdata.dto.MechanicDTO;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.model.boardgame.Mechanic;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditdata.repository.MechanicRepository;
import pl.sankouski.boarditwebapi.service.boardgame.MechanicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MechanicServiceTest {

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private BoardGameRepository boardGameRepository;

    @InjectMocks
    private MechanicService mechanicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllMechanics() {
        when(mechanicRepository.findAll()).thenReturn(List.of(new Mechanic(1L, "Mechanic 1", new ArrayList<>())));

        List<MechanicDTO> result = mechanicService.getAllMechanics();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mechanic 1", result.get(0).getName());
        verify(mechanicRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoMechanics() {
        when(mechanicRepository.findAll()).thenReturn(List.of());

        List<MechanicDTO> result = mechanicService.getAllMechanics();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mechanicRepository, times(1)).findAll();
    }

    @Test
    void shouldGetMechanicById() {
        Mechanic mechanic = new Mechanic(1L, "Mechanic 1", new ArrayList<>());
        when(mechanicRepository.findById(1L)).thenReturn(Optional.of(mechanic));

        MechanicDTO result = mechanicService.getMechanicById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Mechanic 1", result.getName());
        verify(mechanicRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenMechanicNotFoundById() {
        when(mechanicRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> mechanicService.getMechanicById(999L));

        assertEquals("Mechanic not found", exception.getMessage());
        verify(mechanicRepository, times(1)).findById(999L);
    }

    @Test
    void shouldCreateMechanic() {
        MechanicDTO mechanicDTO = new MechanicDTO().setName("New Mechanic");
        Mechanic mechanic = new Mechanic(1L, "New Mechanic", new ArrayList<>());

        when(mechanicRepository.existsByName("New Mechanic")).thenReturn(false);
        when(mechanicRepository.save(any(Mechanic.class))).thenReturn(mechanic);

        MechanicDTO result = mechanicService.createMechanic(mechanicDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Mechanic", result.getName());
        verify(mechanicRepository, times(1)).existsByName("New Mechanic");
        verify(mechanicRepository, times(1)).save(any(Mechanic.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateMechanic() {
        MechanicDTO mechanicDTO = new MechanicDTO().setName("Duplicate Mechanic");

        when(mechanicRepository.existsByName("Duplicate Mechanic")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> mechanicService.createMechanic(mechanicDTO));

        assertEquals("Mechanic with name 'Duplicate Mechanic' already exists.", exception.getMessage());
        verify(mechanicRepository, times(1)).existsByName("Duplicate Mechanic");
        verify(mechanicRepository, never()).save(any());
    }

    @Test
    void shouldUpdateMechanic() {
        Mechanic mechanic = new Mechanic(1L, "Old Mechanic", new ArrayList<>());
        MechanicDTO mechanicDTO = new MechanicDTO().setName("Updated Mechanic");

        when(mechanicRepository.findById(1L)).thenReturn(Optional.of(mechanic));
        when(mechanicRepository.existsByName("Updated Mechanic")).thenReturn(false);
        when(mechanicRepository.save(any(Mechanic.class))).thenReturn(mechanic);

        MechanicDTO result = mechanicService.updateMechanic(1L, mechanicDTO);

        assertNotNull(result);
        assertEquals("Updated Mechanic", result.getName());
        verify(mechanicRepository, times(1)).findById(1L);
        verify(mechanicRepository, times(1)).save(mechanic);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingToDuplicateMechanicName() {
        Mechanic existingMechanic = new Mechanic(1L, "Old Mechanic", new ArrayList<>());
        MechanicDTO mechanicDTO = new MechanicDTO().setName("Duplicate Mechanic");

        when(mechanicRepository.findById(1L)).thenReturn(Optional.of(existingMechanic));
        when(mechanicRepository.existsByName("Duplicate Mechanic")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> mechanicService.updateMechanic(1L, mechanicDTO));

        assertEquals("Mechanic with name 'Duplicate Mechanic' already exists.", exception.getMessage());
        verify(mechanicRepository, times(1)).findById(1L);
        verify(mechanicRepository, never()).save(any());
    }

    @Test
    void shouldDeleteMechanic() {
        Mechanic mechanic = new Mechanic(1L, "Mechanic to Delete", new ArrayList<>());

        when(mechanicRepository.findById(1L)).thenReturn(Optional.of(mechanic));
        when(mechanicRepository.existsById(1L)).thenReturn(true);

        mechanicService.deleteMechanic(1L);

        verify(mechanicRepository, times(1)).findById(1L);
        verify(mechanicRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldRemoveMechanicFromBoardGamesAndDelete() {
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1L);
        boardGame.setName("Board Game 1");
        boardGame.setMechanics(new ArrayList<>());
        Mechanic mechanic = new Mechanic(1L, "Mechanic 1", new ArrayList<>(List.of(boardGame)));

        boardGame.setMechanics(new ArrayList<>(List.of(mechanic)));

        when(mechanicRepository.findById(1L)).thenReturn(Optional.of(mechanic));
        when(mechanicRepository.existsById(1L)).thenReturn(true);

        mechanicService.deleteMechanic(1L);

        assertTrue(boardGame.getMechanics().isEmpty());
        verify(boardGameRepository, times(1)).save(boardGame);
        verify(mechanicRepository, times(1)).deleteById(1L);
    }
}