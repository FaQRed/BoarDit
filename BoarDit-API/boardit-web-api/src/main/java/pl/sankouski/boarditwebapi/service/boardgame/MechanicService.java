package pl.sankouski.boarditwebapi.service.boardgame;


import org.springframework.stereotype.Service;
import pl.sankouski.boarditdata.dto.MechanicDTO;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.model.boardgame.Mechanic;
import pl.sankouski.boarditdata.repository.BoardGameRepository;
import pl.sankouski.boarditdata.repository.MechanicRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MechanicService {
    private final MechanicRepository mechanicRepository;
    private final BoardGameRepository boardGameRepository;

    public MechanicService(MechanicRepository mechanicRepository, BoardGameRepository boardGameRepository) {
        this.mechanicRepository = mechanicRepository;
        this.boardGameRepository = boardGameRepository;
    }

    public List<MechanicDTO> getAllMechanics() {
        return mechanicRepository.findAll()
                .stream()
                .map(mechanic -> new MechanicDTO()
                        .setId(mechanic.getId())
                        .setName(mechanic.getName()))
                .toList();
    }

    public MechanicDTO getMechanicById(Long id) {
        return mechanicRepository.findById(id)
                .map(mechanic -> new MechanicDTO()
                        .setId(mechanic.getId())
                        .setName(mechanic.getName()))
                .orElseThrow(() -> new IllegalArgumentException("Mechanic not found"));
    }

    public MechanicDTO createMechanic(MechanicDTO mechanicDTO) {
        if (mechanicRepository.existsByName(mechanicDTO.getName())) {
            throw new IllegalArgumentException("Mechanic with name '" + mechanicDTO.getName() + "' already exists.");
        }
        Mechanic mechanic = new Mechanic()
                .setName(mechanicDTO.getName());
        Mechanic savedMechanic = mechanicRepository.save(mechanic);
        return new MechanicDTO()
                .setId(savedMechanic.getId())
                .setName(savedMechanic.getName());
    }

    public MechanicDTO updateMechanic(Long id, MechanicDTO mechanicDTO) {

        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mechanic not found"));

        if (!mechanic.getName().equals(mechanicDTO.getName()) &&
                mechanicRepository.existsByName(mechanicDTO.getName())) {
            throw new IllegalArgumentException("Mechanic with name '" + mechanicDTO.getName() + "' already exists.");
        }

        mechanic.setName(mechanicDTO.getName());
        mechanicRepository.save(mechanic);
        return mechanicDTO;
    }

    public void deleteMechanic(Long id) {
        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mechanic with ID " + id + " not found."));


        for (BoardGame boardGame : mechanic.getBoardGames()) {
            boardGame.getMechanics().remove(mechanic);
            boardGameRepository.save(boardGame);
        }
        if (!mechanicRepository.existsById(id)) {
            throw new IllegalArgumentException("Mechanic not found");
        }
        mechanicRepository.deleteById(id);
    }

    public List<MechanicDTO> getMechanicsByIds(List<Long> ids) {
        return mechanicRepository.findAllById(ids).stream()
                .map(mechanic -> new MechanicDTO()
                        .setId(mechanic.getId())
                        .setName(mechanic.getName()))
                .collect(Collectors.toList());
    }
}