package pl.sankouski.boarditupdater.boardGames.mapper;

import pl.sankouski.boarditboardgamesclient.dto.ExpansionDto;
import pl.sankouski.boarditdata.model.boardgame.Expansion;


public class ExpansionMapper {

    public static Expansion mapExpansions(ExpansionDto dto) {
        Expansion expansion = new Expansion();
        expansion.setBggId(dto.getBggId());
        expansion.setName(dto.getPrimaryName());
        expansion.setDescription(dto.getDescription());
        expansion.setYearPublished(dto.getYearPublished().getValue());
        expansion.setMinPlayers(dto.getMinPlayers().getValue());
        expansion.setMaxPlayers(dto.getMaxPlayers().getValue());
        expansion.setPlayingTime(dto.getPlayingTime().getValue());
        expansion.setImageLink(dto.getImageLink());

        return expansion;
    }
}