package pl.sankouski.boarditfront.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestClient;
import pl.sankouski.boarditfront.dto.ArtistDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest
class ArtistServiceTest {

    private MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
    private RestClient.Builder builder = RestClient.builder();
    private ArtistService artistService;

    @BeforeEach
    public void setUp() {
        customizer.customize(builder);
        artistService = new ArtistService(builder.build());
        artistService.setToken("test-token");
    }

    @Test
    void testGetAllArtists() throws Exception {
        String apiUrl = "http://localhost:8080/artists";
        String mockResponse = """
                [
                    {"name": "Artist1", "id": 1},
                    {"name": "Artist2", "id": 2}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<ArtistDTO> result = artistService.getAllArtists();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Artist1");
        assertThat(result.get(1).getName()).isEqualTo("Artist2");
    }

    @Test
    void testGetArtistById() throws Exception {
        String apiUrl = "http://localhost:8080/artists/1";
        String mockResponse = """
                {"name": "Artist1", "id": 1}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        ArtistDTO result = artistService.getArtistById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Artist1");
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    void testCreateArtist() throws Exception {
        String apiUrl = "http://localhost:8080/artists";
        ArtistDTO newArtist = new ArtistDTO(3L, "Artist3" );
        String mockResponse = """
                {"name": "Artist3", "id": 3}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        ArtistDTO result = artistService.createArtist(newArtist);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Artist3");
        assertThat(result.getId()).isEqualTo(3);
    }

    @Test
    void testCreateArtistForBoardGame() throws Exception {
        String apiUrl = "http://localhost:8080/artists/10";
        ArtistDTO newArtist = new ArtistDTO(4L, "Artist4");

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        artistService.createArtistForBoardGame(10L, newArtist);

        assertThat(true).isTrue();
    }

    @Test
    void testUpdateArtist() throws Exception {
        String apiUrl = "http://localhost:8080/artists/1";
        ArtistDTO updatedArtist = new ArtistDTO(1L, "UpdatedArtist");
        String mockResponse = """
                {"name": "UpdatedArtist", "id": 1}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        ArtistDTO result = artistService.updateArtist(1L, updatedArtist);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("UpdatedArtist");
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    void testDeleteArtist() throws Exception {
        String apiUrl = "http://localhost:8080/artists/1";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        artistService.deleteArtist(1L);

        assertThat(true).isTrue();
    }

    @Test
    void testGetArtistsByNames() throws Exception {
        String apiUrl = "http://localhost:8080/artists/names?names=Artist1,Artist2";
        String mockResponse = """
                [
                    {"name": "Artist1", "id": 1},
                    {"name": "Artist2", "id": 2}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<ArtistDTO> result = artistService.getArtistsByNames(List.of("Artist1", "Artist2"));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Artist1");
        assertThat(result.get(1).getName()).isEqualTo("Artist2");
    }
}