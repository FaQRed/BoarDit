package pl.sankouski.boarditwebapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sankouski.boarditdata.dto.AlternateNameDTO;
import pl.sankouski.boarditdata.model.boardgame.AlternateName;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.repository.AlternateNameRepository;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditwebapi.service.boardgame.AlternateNameService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AlternateNameServiceTest {

    @Mock
    private AlternateNameRepository alternateNameRepository;

    @Mock
    private BoardGameRepository boardGameRepository;

    private AlternateNameService alternateNameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        alternateNameService = new AlternateNameService(alternateNameRepository, boardGameRepository);
    }

    @Test
    void shouldGetAlternateNamesByBgId() {
        Long boardGameId = 1L;
        List<AlternateNameDTO> alternateNames = List.of(new AlternateNameDTO("Alternate 1", 1L));

        when(alternateNameRepository.getAlternateNamesByBoardGame_Id(boardGameId)).thenReturn(alternateNames);

        var result = alternateNameService.getAlternateNamesByBgId(boardGameId);

        assertThat(result).isEqualTo(alternateNames);
        verify(alternateNameRepository, times(1)).getAlternateNamesByBoardGame_Id(boardGameId);
    }

    @Test
    void shouldReturnEmptyListIfNoAlternateNames() {
        Long boardGameId = 1L;

        when(alternateNameRepository.getAlternateNamesByBoardGame_Id(boardGameId)).thenReturn(List.of());

        var result = alternateNameService.getAlternateNamesByBgId(boardGameId);

        assertThat(result).isEmpty();
        verify(alternateNameRepository, times(1)).getAlternateNamesByBoardGame_Id(boardGameId);
    }

    @Test
    void shouldGetAlternateNameById() {
        Long id = 1L;
        AlternateName alternateName = new AlternateName();
        alternateName.setId(id);
        alternateName.setName("Alternate 1");

        when(alternateNameRepository.findById(id)).thenReturn(Optional.of(alternateName));

        var result = alternateNameService.getAlternateNameById(id);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(alternateName);
        verify(alternateNameRepository, times(1)).findById(id);
    }


    @Test
    void shouldUpdateAlternateName() {
        AlternateName alternateName = new AlternateName();
        alternateName.setId(1L);
        alternateName.setName("Old Name");

        AlternateNameDTO updatedDto = new AlternateNameDTO();
        updatedDto.setName("New Name");

        when(alternateNameRepository.findById(1L)).thenReturn(Optional.of(alternateName));
        when(alternateNameRepository.findByName("New Name")).thenReturn(Optional.empty());
        when(alternateNameRepository.save(any(AlternateName.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AlternateName result = alternateNameService.updateAlternateName(1L, updatedDto);

        assertThat(result.getName()).isEqualTo("New Name");
        verify(alternateNameRepository).findById(1L);
        verify(alternateNameRepository).findByName("New Name");
        verify(alternateNameRepository).save(alternateName);
    }

    @Test
    void shouldThrowExceptionIfAlternateNameAlreadyExistsOnUpdate() {
        Long id = 1L;
        AlternateName existingAlternateName = new AlternateName();
        existingAlternateName.setId(id);
        existingAlternateName.setName("Old Name");

        AlternateName duplicateAlternateName = new AlternateName();
        duplicateAlternateName.setName("Existing Name");

        AlternateNameDTO alternateNameDTO = new AlternateNameDTO("Existing Name", id);

        when(alternateNameRepository.findById(id)).thenReturn(Optional.of(existingAlternateName));
        when(alternateNameRepository.findByName(alternateNameDTO.getName())).thenReturn(Optional.of(duplicateAlternateName));

        assertThatThrownBy(() -> alternateNameService.updateAlternateName(id, alternateNameDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Alternate name with name 'Existing Name' already exists.");
        verify(alternateNameRepository, times(1)).findById(id);
        verify(alternateNameRepository, times(1)).findByName(alternateNameDTO.getName());
        verify(alternateNameRepository, never()).save(any());
    }

    @Test
    void shouldAddAlternateNameToBoardGame() {
        Long boardGameId = 1L;
        AlternateName alternateName = new AlternateName();
        alternateName.setName("New Alternate");

        BoardGame boardGame = new BoardGame();
        boardGame.setId(boardGameId);

        when(boardGameRepository.findById(boardGameId)).thenReturn(Optional.of(boardGame));
        when(alternateNameRepository.findByName(alternateName.getName())).thenReturn(Optional.empty());
        when(alternateNameRepository.save(alternateName)).thenReturn(alternateName);

        alternateNameService.addAlternateNameToBoardGame(boardGameId, alternateName);

        assertThat(alternateName.getBoardGame()).isEqualTo(boardGame);
        verify(boardGameRepository, times(1)).findById(boardGameId);
        verify(alternateNameRepository, times(1)).findByName(alternateName.getName());
        verify(alternateNameRepository, times(1)).save(alternateName);
    }

    @Test
    void shouldThrowExceptionIfBoardGameNotFoundOnAdd() {
        Long boardGameId = 1L;
        AlternateName alternateName = new AlternateName();
        alternateName.setName("New Alternate");

        when(boardGameRepository.findById(boardGameId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> alternateNameService.addAlternateNameToBoardGame(boardGameId, alternateName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Board game with ID " + boardGameId + " not found");

        verify(boardGameRepository, times(1)).findById(boardGameId);
        verify(alternateNameRepository, never()).save(any());
    }



}