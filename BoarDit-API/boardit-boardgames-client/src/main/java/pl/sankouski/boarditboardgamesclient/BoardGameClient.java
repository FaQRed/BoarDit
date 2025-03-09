package pl.sankouski.boarditboardgamesclient;

import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditboardgamesclient.dto.ExpansionDto;

import java.util.List;

public interface BoardGameClient {

    BoardGameDto getBoardGameByIdFromApi(Long id);

    List<ExpansionDto> getExpansionByIdFromApi(List<Long> id);
}
