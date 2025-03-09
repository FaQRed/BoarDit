package pl.sankouski.boarditupdater.boardGames.updater;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.sankouski.boarditboardgamesclient.BoardGameClient;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;


import pl.sankouski.boarditdata.model.boardgame.BoardGame;
import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;
import pl.sankouski.boarditupdater.boardGames.mapper.BoardGameMapper;


@Service
@Transactional
public class BoardGamesUpdater implements IUpdateBoardGames {

    private final BoarDitDataCatalog data;
    private final BoardGameClient boardGameClient;
    private final BoardGameMapper boardGameMapper;


    public BoardGamesUpdater(BoarDitDataCatalog data, BoardGameClient boardGameClient, BoardGameMapper boardGameMapper) {
        this.data = data;
        this.boardGameClient = boardGameClient;
        this.boardGameMapper = boardGameMapper;
    }

    @Override
    public void updateByIdFromBgg(int num) {


        BoardGameDto boardGameDto = boardGameClient.getBoardGameByIdFromApi((long) num);
        if (boardGameDto == null) {
            return;
        }
        BoardGame boardGame = boardGameMapper.toEntity(boardGameDto, data);

        data.getBoardGame().save(boardGame);


    }
}
