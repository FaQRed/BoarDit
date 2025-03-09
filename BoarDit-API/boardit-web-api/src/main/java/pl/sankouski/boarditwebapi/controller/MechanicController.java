package pl.sankouski.boarditwebapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sankouski.boarditdata.dto.MechanicDTO;
import pl.sankouski.boarditdata.model.boardgame.Mechanic;
import pl.sankouski.boarditwebapi.service.boardgame.MechanicService;

import java.util.List;

@RestController
@RequestMapping("/mechanics")
public class MechanicController {

    private final MechanicService mechanicService;

    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public ResponseEntity<List<MechanicDTO>> getAllMechanics() {
        return ResponseEntity.ok(mechanicService.getAllMechanics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MechanicDTO> getMechanicById(@PathVariable Long id) {
        try {
            mechanicService.getMechanicById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.ok(mechanicService.getMechanicById(id));
    }

    @PostMapping
    public ResponseEntity<MechanicDTO> createMechanic(@RequestBody MechanicDTO mechanicDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mechanicService.createMechanic(mechanicDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MechanicDTO> updateMechanic(@PathVariable Long id, @RequestBody MechanicDTO mechanicDTO) {
        return ResponseEntity.ok(mechanicService.updateMechanic(id, mechanicDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMechanic(@PathVariable Long id) {
        try {
            mechanicService.deleteMechanic(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list/{ids}")
    public ResponseEntity<?> getMechanicsByIds(@PathVariable List<Long> ids) {
        try {
            List<MechanicDTO> mechanics = mechanicService.getMechanicsByIds(ids);
            if (mechanics.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(mechanics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching mechanics: " + e.getMessage());
        }
    }
}