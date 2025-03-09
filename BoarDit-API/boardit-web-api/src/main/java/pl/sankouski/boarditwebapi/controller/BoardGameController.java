package pl.sankouski.boarditwebapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sankouski.boarditdata.dto.BoardGameDTO;
import pl.sankouski.boarditdata.dto.BoardGameUpdateSummaryDTO;
import pl.sankouski.boarditwebapi.service.boardgame.BoardGameService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/boardgames")
public class BoardGameController {

    private final BoardGameService boardGameService;

    public BoardGameController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }


    @GetMapping
    public ResponseEntity<?> getAllBoardGames() {
        try {
            List<BoardGameDTO> boardGames = boardGameService.getAllBoardGames();
            return ResponseEntity.ok(boardGames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error load board games: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardGameById(@PathVariable Long id) {
        try {
            Optional<BoardGameDTO> boardGame = boardGameService.getBoardGameById(id);
            if (boardGame.isPresent()) {
                return  ResponseEntity.ok(boardGame.get());
            } else {
               return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Board game with ID " + id + " not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error load board game: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBoardGame(@RequestBody BoardGameDTO boardGameDTO) {
        try {
            BoardGameDTO createdGame = boardGameService.createBoardGame(boardGameDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error create board game: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBoardGame(@PathVariable Long id, @RequestBody BoardGameUpdateSummaryDTO boardGameDTO) {
        try {
            BoardGameDTO updatedGame = boardGameService.updateBoardGame(id, boardGameDTO);
            return ResponseEntity.ok(updatedGame);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error update board game: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoardGame(@PathVariable Long id) {
        try {
            boardGameService.deleteBoardGame(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error delete board game: " + e.getMessage());
        }
    }

    @PostMapping("/{boardGameId}/categories/{categoryId}")
    public ResponseEntity<String> addCategoryToBoardGame(@PathVariable Long boardGameId, @PathVariable Long categoryId) {
        try {
            boardGameService.addCategoryToBoardGame(boardGameId, categoryId);
            return ResponseEntity.ok("Category added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{boardGameId}/categories/{categoryId}")
    public ResponseEntity<String> removeCategoryFromBoardGame(@PathVariable Long boardGameId, @PathVariable Long categoryId) {
        try {
            boardGameService.removeCategoryFromBoardGame(boardGameId, categoryId);
            return ResponseEntity.ok("Category removed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/{boardGameId}/mechanics/{mechanicId}")
    public ResponseEntity<String> addMechanicToBoardGame(@PathVariable Long boardGameId, @PathVariable Long mechanicId) {
        try {
            boardGameService.addMechanicToBoardGame(boardGameId, mechanicId);
            return ResponseEntity.ok("Mechanic added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{boardGameId}/mechanics/{mechanicId}")
    public ResponseEntity<String> removeMechanicFromBoardGame(@PathVariable Long boardGameId, @PathVariable Long mechanicId) {
        try {
            boardGameService.removeMechanicFromBoardGame(boardGameId, mechanicId);
            return ResponseEntity.ok("Mechanic removed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}