package pl.sankouski.boarditupdater.boardGames.mapper;



import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditdata.model.boardgame.Mechanic;
import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class MechanicMapper {


    @Transactional
    public  List<Mechanic> mapMechanics(BoardGameDto boardGameDto, BoarDitDataCatalog boarDitDataCatalog){
        return boardGameDto.getLinks().stream()
                .filter(link -> "boardgamemechanic".equals(link.getType()))
                .map(link -> {
                    Mechanic mechanic = new Mechanic().setName(link.getValue()).setBggId(Long.parseLong(link.getId()));
                    Mechanic existingMechanic = boarDitDataCatalog.getMechanics().findByBggId(mechanic.getBggId()).orElse(null);

                    if(existingMechanic == null){
                        boarDitDataCatalog.getMechanics().save(mechanic);
                        return mechanic;
                    }
                    return existingMechanic;
                })
                .collect(Collectors.toList());
    }




}