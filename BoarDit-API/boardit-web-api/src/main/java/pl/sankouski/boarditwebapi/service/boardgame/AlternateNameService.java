package pl.sankouski.boarditwebapi.service.boardgame;


import org.springframework.stereotype.Service;
import pl.sankouski.boarditdata.dto.AlternateNameDTO;
import pl.sankouski.boarditdata.model.boardgame.AlternateName;
import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.repository.AlternateNameRepository;
import pl.sankouski.boarditdata.repository.BoardGameRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlternateNameService {

    private final AlternateNameRepository alternateNameRepository;
    private final BoardGameRepository boardGameRepository;

    public AlternateNameService(AlternateNameRepository alternateNameRepository, BoardGameRepository boardGameRepository) {
        this.alternateNameRepository = alternateNameRepository;
        this.boardGameRepository = boardGameRepository;
    }

    public List<AlternateNameDTO> getAlternateNamesByBgId(Long boardGameId){
        return alternateNameRepository.getAlternateNamesByBoardGame_Id(boardGameId);
    }


    public Optional<AlternateName> getAlternateNameById(Long id) {
        return alternateNameRepository.findById(id);
    }

    public AlternateName updateAlternateName(Long id, AlternateNameDTO alternateNameDTO) {
        AlternateName alternateName = alternateNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alternate name with ID " + id + " not found"));

        if (!alternateName.getName().equals(alternateNameDTO.getName()) &&
                alternateNameRepository.findByName(alternateNameDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Alternate name with name '" + alternateNameDTO.getName() + "' already exists.");
        }

        alternateName.setName(alternateNameDTO.getName());
        return alternateNameRepository.save(alternateName);
    }

    public void deleteAlternateName(Long id) {
        alternateNameRepository.deleteById(id);
    }

    public void addAlternateNameToBoardGame(Long boardGameId, AlternateName alternateName) {
        BoardGame boardGame = boardGameRepository.findById(boardGameId)
                .orElseThrow(() -> new IllegalArgumentException("Board game with ID " + boardGameId + " not found"));

        if (alternateNameRepository.findByName(alternateName.getName()).isPresent()){
            throw new IllegalArgumentException("Alternate name with name '" + alternateName.getName() + "' already exists.");
        }
        alternateName.setBoardGame(boardGame);
        alternateNameRepository.save(alternateName);
    }
}
