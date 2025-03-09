package pl.sankouski.boarditwebapi.service.boardgame;

import org.springframework.stereotype.Service;
import pl.sankouski.boarditdata.dto.ArtistDTO;
import pl.sankouski.boarditdata.model.boardgame.Artist;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.repository.ArtistRepository;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditdata.repository.ICatalogData;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final BoardGameRepository boardGameRepository;

    public ArtistService(ArtistRepository artistRepository, BoardGameRepository boardGameRepository) {
        this.artistRepository = artistRepository;
        this.boardGameRepository = boardGameRepository;
    }

    public ArtistDTO convertToDTO(Artist artist) {
        return new ArtistDTO(artist.getId(), artist.getName());
    }

    public Artist convertFromDTO(ArtistDTO dto) {
        Artist artist = new Artist();
        artist.setId(dto.getId());
        artist.setName(dto.getName());
        return artist;
    }

    public List<ArtistDTO> getAllArtists() {
        return artistRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ArtistDTO> getArtistById(Long id) {
        return artistRepository.findById(id)
                .map(this::convertToDTO);
    }

    public ArtistDTO createArtist(ArtistDTO artistDTO) {
        if (artistRepository.existsByName(artistDTO.getName())) {
            throw new IllegalArgumentException("Artist with name '" + artistDTO.getName() + "' already exists.");
        }
        Artist artist = convertFromDTO(artistDTO);
        Artist savedArtist = artistRepository.save(artist);
        return convertToDTO(savedArtist);
    }


    public ArtistDTO updateArtist(Long id, ArtistDTO artistDTO) {
        Artist existingArtist = artistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Artist with ID " + id + " not found."));

        if (!existingArtist.getName().equals(artistDTO.getName()) &&
                artistRepository.existsByName(artistDTO.getName())) {
            throw new IllegalArgumentException("Artist with name '" + artistDTO.getName() + "' already exists.");
        }

        existingArtist.setName(artistDTO.getName());
        Artist updatedArtist = artistRepository.save(existingArtist);
        return convertToDTO(updatedArtist);
    }

    public void deleteArtist(Long id) {

        if (!artistRepository.existsById(id)) {
            throw new IllegalArgumentException("Artist with ID " + id + " not found.");
        }
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Artist with ID " + id + " not found."));

        for (BoardGame boardGame : artist.getBoardGames()) {
            boardGame.getArtists().remove(artist);
            boardGameRepository.save(boardGame);
        }
        artistRepository.deleteById(id);
    }


    public List<ArtistDTO> getArtistsByNames(List<String> names) {
      List<ArtistDTO> artistDTOS = new ArrayList<>();

      for (String name : names){
          artistDTOS.add(convertToDTO(Objects.requireNonNull(artistRepository.findByName(name).orElse(null))));
      }
      return artistDTOS;
    }

    public void createArtistForBoardGame(Long boardGameId, ArtistDTO artistDTO) {
        Artist artist = convertFromDTO(artistDTO);

        BoardGame boardGame = boardGameRepository.findById(boardGameId)
                .orElseThrow(() -> new IllegalArgumentException("BoardGame with ID " + boardGameId + " not found."));

        if (boardGame.getArtists().stream().anyMatch(a -> a.getName().equalsIgnoreCase(artist.getName()))) {
            throw new IllegalArgumentException("Artist with the same name already exists for this board game.");
        }
        artistRepository.save(artist);
        boardGame.getArtists().add(artist);

        boardGameRepository.save(boardGame);
    }
}