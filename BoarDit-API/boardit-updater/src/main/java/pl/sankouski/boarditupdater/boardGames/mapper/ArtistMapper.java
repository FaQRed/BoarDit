package pl.sankouski.boarditupdater.boardGames.mapper;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditdata.model.boardgame.Artist;
import pl.sankouski.boarditdata.model.boardgame.Category;
import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArtistMapper {

    @Transactional
    public  List<Artist> mapArtist(BoardGameDto boardGameDto, BoarDitDataCatalog boarDitDataCatalog){
        return boardGameDto.getLinks().stream()
                .filter(link -> "boardgameartist".equals(link.getType()))
                .map(link -> {
                    Artist artist = new Artist().setName(link.getValue()).setBggId(Long.parseLong(link.getId()));
                    Artist existingArtist = boarDitDataCatalog.getArtists().findByBggId(artist.getBggId()).orElse(null);

                    if(existingArtist == null){
                        boarDitDataCatalog.getArtists().save(artist);
                        return artist;
                    }
                    return existingArtist;
                })
                .collect(Collectors.toList());
    }
}