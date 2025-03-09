package pl.sankouski.boarditupdater.boardGames.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sankouski.boarditboardgamesclient.BoardGameClient;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditboardgamesclient.dto.ExpansionDto;
import pl.sankouski.boarditboardgamesclient.exception.GameAlreadyExistsException;
import pl.sankouski.boarditdata.model.boardgame.*;

import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class BoardGameMapper {


    private final BoardGameClient boardGameClient;
    private final MechanicMapper mechanicMapper;
    private final ArtistMapper artistMapper;
    private final CategoryMapper categoryMapper;


    @Autowired
    public BoardGameMapper(BoardGameClient boardGameClient, MechanicMapper mechanicMapper, ArtistMapper artistMapper, CategoryMapper categoryMapper) {
        this.boardGameClient = boardGameClient;
        this.mechanicMapper = mechanicMapper;
        this.artistMapper = artistMapper;
        this.categoryMapper = categoryMapper;
    }

    public BoardGame toEntity(BoardGameDto dto, BoarDitDataCatalog boarDitDataCatalog) {

        Optional<BoardGame> existingBoardGame = boarDitDataCatalog.getBoardGame().findByBggId(dto.getBggId());
        if (existingBoardGame.isPresent()){
            throw new GameAlreadyExistsException("Game with this ID from BoardGamesGeek already exists");
        }
        BoardGame boardGame = new BoardGame();


        boardGame.setName(dto.getPrimaryName());
        boardGame.setDescription(dto.getDescription());
        boardGame.setYearPublished(dto.getYearPublished().getValue());
        boardGame.setMinPlayers(dto.getMinPlayers().getValue());
        boardGame.setMaxPlayers(dto.getMaxPlayers().getValue());
        boardGame.setPlayingTime(dto.getPlayingTime().getValue());
        boardGame.setImageLink(dto.getImageLink());
        boardGame.setBggId(dto.getBggId());

        boardGame.setAlternateNames(dto.getAlternateNames().stream()
                .map(name -> new AlternateName().setName(name).setBoardGame(boardGame))
                .collect(Collectors.toList()));



        List<Long> expansionsId = dto.getLinks().stream()
                .filter(link -> "boardgameexpansion".equals(link.getType()))
                .map(link -> Long.parseLong(link.getId())).toList();



        List<ExpansionDto> expansionsDto = boardGameClient.getExpansionByIdFromApi(expansionsId);


        List<Expansion> expansions = expansionsDto.stream()
                .map(ExpansionMapper::mapExpansions)
                .peek(expansion -> expansion.setBoardGame(boardGame))
                .toList();


        List<Mechanic> mechanics = mechanicMapper.mapMechanics(dto, boarDitDataCatalog);
        List<Artist> artists = artistMapper.mapArtist(dto, boarDitDataCatalog);
        List<Category> categories = categoryMapper.mapCategory(dto, boarDitDataCatalog);






        boardGame.setArtists(artists);
        boardGame.setCategories(categories);

        boardGame.setMechanics(mechanics);
        boardGame.setExpansions(expansions);



        return boardGame;
    }
}