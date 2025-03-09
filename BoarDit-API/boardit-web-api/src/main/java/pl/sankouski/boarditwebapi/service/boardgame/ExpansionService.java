package pl.sankouski.boarditwebapi.service.boardgame;

import org.springframework.stereotype.Service;
import pl.sankouski.boarditdata.dto.ExpansionDTO;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.model.boardgame.Expansion;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditdata.repository.ExpansionRepository;

import java.time.Year;
import java.util.List;

@Service
public class ExpansionService {

    private final ExpansionRepository expansionRepository;
    private final BoardGameRepository boardGameRepository;

    public ExpansionService(ExpansionRepository expansionRepository, BoardGameRepository boardGameRepository) {
        this.expansionRepository = expansionRepository;
        this.boardGameRepository = boardGameRepository;
    }

    public List<ExpansionDTO> getAllExpansions() {
        return expansionRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public ExpansionDTO getExpansionById(Long id) {
        Expansion expansion = expansionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expansion with ID " + id + " not found."));
        return convertToDTO(expansion);
    }

    public ExpansionDTO createExpansion(Long boardGameId, ExpansionDTO expansionDTO) {
        validateExpansion(expansionDTO);

        BoardGame boardGame = boardGameRepository.findById(boardGameId)
                .orElseThrow(() -> new IllegalArgumentException("Board game with ID " + boardGameId + " not found."));

        Expansion expansion = new Expansion()
                .setName(expansionDTO.getName())
                .setDescription(expansionDTO.getDescription())
                .setYearPublished(expansionDTO.getYearPublished())
                .setMinPlayers(expansionDTO.getMinPlayers())
                .setMaxPlayers(expansionDTO.getMaxPlayers())
                .setPlayingTime(expansionDTO.getPlayingTime())
                .setImageLink(expansionDTO.getImageLink())
                .setBoardGame(boardGame);

        Expansion savedExpansion = expansionRepository.save(expansion);
        return convertToDTO(savedExpansion);
    }

    public ExpansionDTO updateExpansion(Long boardGameId, ExpansionDTO expansionDTO) {
        validateExpansion(expansionDTO);

        Expansion expansion = expansionRepository.findById(expansionDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Expansion with ID " + expansionDTO.getId() + " not found."));

        BoardGame boardGame = boardGameRepository.findById(boardGameId)
                .orElseThrow(() -> new IllegalArgumentException("Board game with ID " + boardGameId + " not found."));

        expansion.setName(expansionDTO.getName())
                .setDescription(expansionDTO.getDescription())
                .setYearPublished(expansionDTO.getYearPublished())
                .setMinPlayers(expansionDTO.getMinPlayers())
                .setMaxPlayers(expansionDTO.getMaxPlayers())
                .setPlayingTime(expansionDTO.getPlayingTime())
                .setImageLink(expansionDTO.getImageLink())
                .setBoardGame(boardGame);

        Expansion updatedExpansion = expansionRepository.save(expansion);
        return convertToDTO(updatedExpansion);
    }

    public void deleteExpansion(Long id) {
        Expansion expansion = expansionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expansion with ID " + id + " not found."));
        expansionRepository.delete(expansion);
    }

    private ExpansionDTO convertToDTO(Expansion expansion) {
        return new ExpansionDTO()
                .setId(expansion.getId())
                .setName(expansion.getName())
                .setDescription(expansion.getDescription())
                .setYearPublished(expansion.getYearPublished())
                .setMinPlayers(expansion.getMinPlayers())
                .setMaxPlayers(expansion.getMaxPlayers())
                .setPlayingTime(expansion.getPlayingTime())
                .setImageLink(expansion.getImageLink());
    }

    public List<ExpansionDTO> getAllExpansionsByBoardGameId(Long boardGameId) {
        return expansionRepository.getExpansionsByBoardGame_Id(boardGameId);
    }


    private void validateExpansion(ExpansionDTO expansionDTO) {
        int currentYear = Year.now().getValue();

        if (expansionDTO.getYearPublished() < 1900 || expansionDTO.getYearPublished() > currentYear) {
            throw new IllegalArgumentException("Year Published must be between 1900 and " + currentYear);
        }

        if (expansionDTO.getMinPlayers() < 1) {
            throw new IllegalArgumentException("Minimum number of players must be at least 1.");
        }

        if (expansionDTO.getMaxPlayers() < expansionDTO.getMinPlayers()) {
            throw new IllegalArgumentException("Maximum number of players cannot be less than minimum players.");
        }

        if (expansionDTO.getPlayingTime() < 0) {
            throw new IllegalArgumentException("Playing time cannot be negative.");
        }

        if (expansionDTO.getName() == null || expansionDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Expansion name cannot be empty.");
        }

        if (expansionDTO.getDescription() == null || expansionDTO.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Expansion description cannot be empty.");
        }
    }
}