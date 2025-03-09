package pl.sankouski.boarditdata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sankouski.boarditdata.repository.*;

import static org.junit.jupiter.api.Assertions.*;

public class BoarDitDataCatalogTest {

    @Mock
    private AlternateNameRepository alternateNameRepository;
    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private BoardGameRepository boardGameRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ExpansionRepository expansionRepository;
    @Mock
    private MechanicRepository mechanicRepository;
    @Mock
    private UserRepository userRepository;

    private BoarDitDataCatalog catalog;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        catalog = new BoarDitDataCatalog(
                alternateNameRepository,
                artistRepository,
                boardGameRepository,
                categoryRepository,
                expansionRepository,
                mechanicRepository,
                userRepository
        );
    }

    @Test
    public void getAlternateName_shouldReturnAlternateNameRepository() {
        assertEquals(alternateNameRepository, catalog.getAlternateName());
    }

    @Test
    public void getArtists_shouldReturnArtistRepository() {
        assertEquals(artistRepository, catalog.getArtists());
    }

    @Test
    public void getBoardGame_shouldReturnBoardGameRepository() {
        assertEquals(boardGameRepository, catalog.getBoardGame());
    }

    @Test
    public void getCategories_shouldReturnCategoryRepository() {
        assertEquals(categoryRepository, catalog.getCategories());
    }

    @Test
    public void getExpansions_shouldReturnExpansionRepository() {
        assertEquals(expansionRepository, catalog.getExpansions());
    }

    @Test
    public void getMechanics_shouldReturnMechanicRepository() {
        assertEquals(mechanicRepository, catalog.getMechanics());
    }

    @Test
    public void getUsers_shouldReturnUserRepository() {
        assertEquals(userRepository, catalog.getUsers());
    }
}