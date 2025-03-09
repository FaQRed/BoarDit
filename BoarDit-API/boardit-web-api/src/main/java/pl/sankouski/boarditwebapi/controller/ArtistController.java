package pl.sankouski.boarditwebapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sankouski.boarditdata.dto.ArtistDTO;
import pl.sankouski.boarditwebapi.service.boardgame.ArtistService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArtistById(@PathVariable Long id) {
        Optional<ArtistDTO> artist = artistService.getArtistById(id);
        if (artist.isPresent()) {
            return ResponseEntity.ok(artist.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artist not found.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createArtist(@RequestBody ArtistDTO artistDTO) {
        try {
            ArtistDTO createdArtist = artistService.createArtist(artistDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdArtist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/{boardGameId}")
    public ResponseEntity<?> createArtistForBoardGame(@PathVariable Long boardGameId, @RequestBody ArtistDTO artistDTO) {
        try {
            artistService.createArtistForBoardGame(boardGameId, artistDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtist(@PathVariable Long id, @RequestBody ArtistDTO artistDTO) {
        try {
            ArtistDTO updatedArtist = artistService.updateArtist(id, artistDTO);
            return ResponseEntity.ok(updatedArtist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtist(@PathVariable Long id) {
        try {
            artistService.deleteArtist(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/names")
    public ResponseEntity<?> getArtistsByNames(@RequestParam List<String> names) {
        try {
            List<ArtistDTO> artists = artistService.getArtistsByNames(names);
            if (artists.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(artists);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching artists: " + e.getMessage());
        }
    }
}