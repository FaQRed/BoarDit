package pl.sankouski.boarditwebapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sankouski.boarditdata.dto.ExpansionDTO;
import pl.sankouski.boarditwebapi.service.boardgame.ExpansionService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/boardgames")
public class ExpansionController {

    private final ExpansionService expansionService;

    public ExpansionController(ExpansionService expansionService) {
        this.expansionService = expansionService;
    }

    @GetMapping("/expansions")
    public ResponseEntity<List<ExpansionDTO>> getAllExpansions() {
        if(expansionService.getAllExpansions().isEmpty()){
            return ResponseEntity.ok(new ArrayList<>());
        }

        return ResponseEntity.ok(expansionService.getAllExpansions());
    }

    @GetMapping("/{boardGameId}/expansions")
    public ResponseEntity<?> getAllExpansionsByBoardGameId(@PathVariable Long boardGameId) {


        try {
            return ResponseEntity.ok(expansionService.getAllExpansionsByBoardGameId(boardGameId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while getting alternate names: " + e.getMessage());
        }
    }

    @GetMapping("/expansions/{id}")
    public ResponseEntity<?> getExpansionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(expansionService.getExpansionById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{boardGameId}/expansions")
    public ResponseEntity<?> createExpansion(@PathVariable Long boardGameId, @RequestBody ExpansionDTO expansionDTO) {
        try {
            ExpansionDTO savedExpansion = expansionService.createExpansion(boardGameId, expansionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedExpansion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{boardGameId}/expansions/{id}")
    public ResponseEntity<?> updateExpansion(@PathVariable Long boardGameId, @PathVariable Long id, @RequestBody ExpansionDTO expansionDTO) {
        try {
            expansionDTO.setId(id);
            ExpansionDTO updatedExpansion = expansionService.updateExpansion(boardGameId, expansionDTO);
            return ResponseEntity.ok(updatedExpansion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/expansions/{id}")
    public ResponseEntity<?> deleteExpansion(@PathVariable Long id) {
        try {
            expansionService.deleteExpansion(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}