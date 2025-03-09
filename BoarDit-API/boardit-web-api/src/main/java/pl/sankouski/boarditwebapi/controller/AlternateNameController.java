package pl.sankouski.boarditwebapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.sankouski.boarditdata.dto.AlternateNameDTO;
import pl.sankouski.boarditdata.model.boardgame.AlternateName;
import pl.sankouski.boarditwebapi.service.boardgame.AlternateNameService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/boardgames/{boardGameId}/alternatenames")
public class AlternateNameController {

    private final AlternateNameService alternateNameService;

    public AlternateNameController(AlternateNameService alternateNameService) {
        this.alternateNameService = alternateNameService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllAlternateNamesByGameId(@PathVariable Long boardGameId) {
        try {
            List<AlternateNameDTO> alternateNames = alternateNameService.getAlternateNamesByBgId(boardGameId);
            return ResponseEntity.ok(alternateNames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while getting alternate names: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAlternateNameById(@PathVariable Long id) {
        try {
            AlternateName alternateName = alternateNameService.getAlternateNameById(id).get();
            AlternateNameDTO alternateNameDto = new AlternateNameDTO(alternateName.getName(), alternateName.getId());
            return ResponseEntity.ok(alternateNameDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while getting alternate names: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAlternateName(@PathVariable Long boardGameId, @RequestBody AlternateNameDTO alternateNameDTO, BindingResult result) {
        try {
            AlternateName alternateName = new AlternateName();
            alternateName.setName(alternateNameDTO.getName());

            alternateNameService.addAlternateNameToBoardGame(boardGameId, alternateName);

            return ResponseEntity.status(HttpStatus.CREATED).body("Alternate name created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error while creating alternate name: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlternateName(@PathVariable Long id, @RequestBody AlternateNameDTO alternateNameDTO) {
        try {
            Optional<AlternateName> existingAlternateName = alternateNameService.getAlternateNameById(id);

            if (existingAlternateName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Alternate name with ID " + id + " not found.");
            }

            AlternateName updatedAlternateName = alternateNameService.updateAlternateName(id, alternateNameDTO);
            return ResponseEntity.ok(updatedAlternateName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error update alternate name: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAlternateName(@PathVariable Long id) {
        try {
            if (alternateNameService.getAlternateNameById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Alternate name with ID " + id + " not found.");
            }
            alternateNameService.deleteAlternateName(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while deleting alternate name: " + e.getMessage());
        }
    }
}