package pl.sankouski.boarditwebapi.service.boardgame;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.sankouski.boarditdata.dto.BoardGameDTO;
import pl.sankouski.boarditdata.dto.BoardGameUpdateSummaryDTO;
import pl.sankouski.boarditdata.model.boardgame.*;
import pl.sankouski.boarditdata.repository.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;
    private final CategoryRepository categoryRepository;
    private final MechanicRepository mechanicRepository;
    private final ArtistRepository artistRepository;
    private final AlternateNameRepository alternateNameRepository;

    public BoardGameService(BoardGameRepository boardGameRepository,
                            CategoryRepository categoryRepository,
                            MechanicRepository mechanicRepository,
                            ArtistRepository artistRepository,
                            AlternateNameRepository alternateNameRepository) {
        this.boardGameRepository = boardGameRepository;
        this.categoryRepository = categoryRepository;
        this.mechanicRepository = mechanicRepository;
        this.artistRepository = artistRepository;
        this.alternateNameRepository = alternateNameRepository;
    }

    @Transactional
    public List<BoardGameDTO> getAllBoardGames() {
        return boardGameRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public Optional<BoardGameDTO> getBoardGameById(Long id) {
        return boardGameRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public BoardGameDTO createBoardGame(BoardGameDTO boardGameDTO) {

        validateBoardGameDTO(boardGameDTO);


        if (boardGameRepository.findByName(boardGameDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("A board game with the name '" + boardGameDTO.getName() + "' already exists.");
        }


        BoardGame boardGame = new BoardGame();
        boardGame.setName(boardGameDTO.getName());
        boardGame.setDescription(boardGameDTO.getDescription());
        boardGame.setYearPublished(boardGameDTO.getYearPublished());
        boardGame.setMinPlayers(boardGameDTO.getMinPlayers());
        boardGame.setMaxPlayers(boardGameDTO.getMaxPlayers());
        boardGame.setPlayingTime(boardGameDTO.getPlayingTime());
        boardGame.setImageLink(boardGameDTO.getImageLink());
        boardGame.setCategories(categoryRepository.findAllById(boardGameDTO.getCategories()));
        boardGame.setMechanics(mechanicRepository.findAllById(boardGameDTO.getMechanics()));


        boardGame = boardGameRepository.save(boardGame);


        List<Artist> artists = new ArrayList<>();
        if (boardGameDTO.getArtists() != null) {
            for (String artistName : boardGameDTO.getArtists()) {
                BoardGame finalBoardGame = boardGame;
                Artist artist = artistRepository.findByName(artistName)
                        .orElseGet(() -> {
                            Artist newArtist = new Artist();
                            newArtist.setName(artistName);
                            newArtist.setBoardGames(List.of(finalBoardGame));
                            return artistRepository.save(newArtist);
                        });
                artists.add(artist);
            }
        }


        List<AlternateName> alternateNames = new ArrayList<>();
        if (boardGameDTO.getAlternateNames() != null) {
            for (String alternateName : boardGameDTO.getAlternateNames()) {
                BoardGame finalBoardGame1 = boardGame;
                AlternateName altName = alternateNameRepository.findByName(alternateName)
                        .orElseGet(() -> {
                            AlternateName newAltName = new AlternateName();
                            newAltName.setName(alternateName);
                            newAltName.setBoardGame(finalBoardGame1);
                            return alternateNameRepository.save(newAltName);
                        });
                alternateNames.add(altName);
            }
        }


        boardGame.setArtists(artists);
        boardGame.setAlternateNames(alternateNames);


        boardGame = boardGameRepository.save(boardGame);


        return convertToDTO(boardGame);
    }

    @Transactional
    public BoardGameDTO updateBoardGame(Long id, BoardGameUpdateSummaryDTO updateSummaryDTO) {

        validateBoardGameUpdateSummaryDTO(updateSummaryDTO);

        BoardGame boardGame = boardGameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board game with ID " + id + " not found."));
        if (!boardGame.getName().equals(updateSummaryDTO.getName()) &&
                boardGameRepository.findByName(updateSummaryDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("A board game with the name '" + updateSummaryDTO.getName() + "' already exists.");
        }

        boardGame.setName(updateSummaryDTO.getName());
        boardGame.setDescription(updateSummaryDTO.getDescription());
        boardGame.setYearPublished(updateSummaryDTO.getYearPublished());
        boardGame.setMinPlayers(updateSummaryDTO.getMinPlayers());
        boardGame.setMaxPlayers(updateSummaryDTO.getMaxPlayers());
        boardGame.setPlayingTime(updateSummaryDTO.getPlayingTime());
        boardGame.setImageLink(updateSummaryDTO.getImageLink());

        return convertToDTO(boardGameRepository.save(boardGame));
    }

    @Transactional
    public void deleteBoardGame(Long id) {
        if (!boardGameRepository.existsById(id)) {
            throw new IllegalArgumentException("Board game with ID " + id + " not found.");
        }
        boardGameRepository.deleteById(id);
    }

    public void validateBoardGameDTO(BoardGameDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Board game name is required.");
        }
        if (dto.getYearPublished() <= 0) {
            throw new IllegalArgumentException("Year published must be greater than zero.");
        }
        if (dto.getMinPlayers() <= 0 || dto.getMaxPlayers() <= 0 || dto.getMinPlayers() > dto.getMaxPlayers()) {
            throw new IllegalArgumentException("Invalid player range. Ensure that minPlayers > 0, maxPlayers > 0, and minPlayers <= maxPlayers.");
        }
        if (dto.getCategories() == null || dto.getCategories().isEmpty()) {
            throw new IllegalArgumentException("At least one category is required.");
        }
        if (dto.getMechanics() == null || dto.getMechanics().isEmpty()) {
            throw new IllegalArgumentException("At least one mechanic is required.");
        }
    }
    public void addCategoryToBoardGame(Long boardGameId, Long categoryId) {
        BoardGame boardGame = boardGameRepository.findById(boardGameId)
                .orElseThrow(() -> new IllegalArgumentException("BoardGame not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (boardGame.getCategories().contains(category)) {
            throw new IllegalArgumentException("Category already associated with this BoardGame.");
        }
        boardGame.getCategories().add(category);
        boardGameRepository.save(boardGame);
    }

    public void removeCategoryFromBoardGame(Long boardGameId, Long categoryId) {
        BoardGame boardGame = boardGameRepository.findById(boardGameId)
                .orElseThrow(() -> new IllegalArgumentException("BoardGame not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (!boardGame.getCategories().contains(category)) {
            throw new IllegalArgumentException("Category is not associated with this BoardGame.");
        }
        boardGame.getCategories().remove(category);
        boardGameRepository.save(boardGame);
    }

    public void addMechanicToBoardGame(Long boardGameId, Long mechanicId) {
        BoardGame boardGame = boardGameRepository.findById(boardGameId)
                .orElseThrow(() -> new IllegalArgumentException("BoardGame not found"));
        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new IllegalArgumentException("Mechanic not found"));

        if (boardGame.getMechanics().contains(mechanic)) {
            throw new IllegalArgumentException("Mechanic already associated with this BoardGame.");
        }
        boardGame.getMechanics().add(mechanic);
        boardGameRepository.save(boardGame);
    }

    public void removeMechanicFromBoardGame(Long boardGameId, Long mechanicId) {
        BoardGame boardGame = boardGameRepository.findById(boardGameId)
                .orElseThrow(() -> new IllegalArgumentException("BoardGame not found"));
        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new IllegalArgumentException("Mechanic not found"));

        if (!boardGame.getMechanics().contains(mechanic)) {
            throw new IllegalArgumentException("Mechanic is not associated with this BoardGame.");
        }
        boardGame.getMechanics().remove(mechanic);
        boardGameRepository.save(boardGame);
    }

    public void validateBoardGameUpdateSummaryDTO(BoardGameUpdateSummaryDTO updateSummaryDTO) {
        if (updateSummaryDTO.getName() == null || updateSummaryDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Board game name cannot be empty.");
        }
        if (updateSummaryDTO.getYearPublished() <= 0) {
            throw new IllegalArgumentException("Year published must be greater than 0.");
        }
        if (updateSummaryDTO.getMinPlayers() <= 0 || updateSummaryDTO.getMaxPlayers() <= 0) {
            throw new IllegalArgumentException("Number of players must be greater than 0.");
        }
        if (updateSummaryDTO.getMinPlayers() > updateSummaryDTO.getMaxPlayers()) {
            throw new IllegalArgumentException("Min players cannot be greater than max players.");
        }
        if (updateSummaryDTO.getPlayingTime() <= 0) {
            throw new IllegalArgumentException("Playing time must be greater than 0.");
        }
        if (updateSummaryDTO.getImageLink() != null && !updateSummaryDTO.getImageLink().startsWith("http")) {
            throw new IllegalArgumentException("Image link must be a valid URL.");
        }
    }



    private BoardGameDTO convertToDTO(BoardGame boardGame) {
        BoardGameDTO dto = new BoardGameDTO();
        dto.setId(boardGame.getId());
        dto.setName(boardGame.getName());
        dto.setDescription(boardGame.getDescription());
        dto.setYearPublished(boardGame.getYearPublished());
        dto.setMinPlayers(boardGame.getMinPlayers());
        dto.setMaxPlayers(boardGame.getMaxPlayers());
        dto.setPlayingTime(boardGame.getPlayingTime());
        dto.setImageLink(boardGame.getImageLink());
        dto.setCategories(Optional.ofNullable(boardGame.getCategories())
                .orElse(Collections.emptyList())
                .stream()
                .map(Category::getId)
                .toList());
        dto.setMechanics(Optional.ofNullable(boardGame.getMechanics())
                .orElse(Collections.emptyList())
                .stream()
                .map(Mechanic::getId)
                .toList());
        dto.setArtists(Optional.ofNullable(boardGame.getArtists())
                .orElse(Collections.emptyList())
                .stream()
                .map(Artist::getName)
                .toList());
        dto.setAlternateNames(Optional.ofNullable(boardGame.getAlternateNames())
                .orElse(Collections.emptyList())
                .stream()
                .map(AlternateName::getName)
                .toList());
        return dto;
    }
}