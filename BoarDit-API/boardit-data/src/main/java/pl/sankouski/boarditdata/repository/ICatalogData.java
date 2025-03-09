package pl.sankouski.boarditdata.repository;

public interface ICatalogData {
    AlternateNameRepository getAlternateName();
    ArtistRepository getArtists();
    BoardGameRepository getBoardGame();
    CategoryRepository getCategories();
    ExpansionRepository getExpansions();
    MechanicRepository getMechanics();
    UserRepository getUsers();
}
