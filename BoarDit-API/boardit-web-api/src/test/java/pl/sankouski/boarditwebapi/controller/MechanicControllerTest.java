package pl.sankouski.boarditwebapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import pl.sankouski.boarditdata.dto.MechanicDTO;
import pl.sankouski.boarditwebapi.service.boardgame.MechanicService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MechanicControllerTest {

    @Mock
    private MechanicService mechanicService;

    @InjectMocks
    private MechanicController mechanicController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnMechanicsByIds() {
        List<Long> ids = List.of(1L, 2L);
        List<MechanicDTO> mechanics = List.of(
                new MechanicDTO().setId(1L).setName("Mechanic 1"),
                new MechanicDTO().setId(2L).setName("Mechanic 2")
        );

        when(mechanicService.getMechanicsByIds(ids)).thenReturn(mechanics);

        ResponseEntity<?> response = mechanicController.getMechanicsByIds(ids);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mechanics, response.getBody());
        verify(mechanicService, times(1)).getMechanicsByIds(ids);
    }

    @Test
    void shouldReturnNotFoundIfMechanicsListIsEmpty() {

        List<Long> ids = List.of(1L, 2L);

        when(mechanicService.getMechanicsByIds(ids)).thenReturn(List.of());

        ResponseEntity<?> response = mechanicController.getMechanicsByIds(ids);
        assertEquals(404, response.getStatusCodeValue());
        verify(mechanicService, times(1)).getMechanicsByIds(ids);
    }

    @Test
    void shouldHandleInternalServerError() {
        List<Long> ids = List.of(1L, 2L);

        when(mechanicService.getMechanicsByIds(ids)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = mechanicController.getMechanicsByIds(ids);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error fetching mechanics: Unexpected error", response.getBody());
        verify(mechanicService, times(1)).getMechanicsByIds(ids);
    }
}