package pl.sankouski.boarditdata.repository;


import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository

public class BoarDitDataCatalog implements ICatalogData {

    private final AlternateNameRepository alternateName;
    private final ArtistRepository artist;
    private final BoardGameRepository boardGame;
    private final CategoryRepository category;
    private final ExpansionRepository expansion;
    private final MechanicRepository mechanic;
    private final UserRepository users;

    public BoarDitDataCatalog(AlternateNameRepository alternateName, ArtistRepository artist,
                              BoardGameRepository boardGame, CategoryRepository category, ExpansionRepository expansion, MechanicRepository mechanic, UserRepository users) {
        this.alternateName = alternateName;
        this.artist = artist;
        this.boardGame = boardGame;
        this.category = category;
        this.expansion = expansion;
        this.mechanic = mechanic;
        this.users = users;
    }

    public AlternateNameRepository getAlternateName() {
        return alternateName;
    }

    public ArtistRepository getArtists() {
        return artist;
    }

    public BoardGameRepository getBoardGame() {
        return boardGame;
    }

    public CategoryRepository getCategories() {
        return category;
    }

    public ExpansionRepository getExpansions() {
        return expansion;
    }

    public MechanicRepository getMechanics() {
        return mechanic;
    }


    public UserRepository getUsers() {
        return users;
    }


}
