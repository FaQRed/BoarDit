package pl.sankouski.boarditfront.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestClient;
import pl.sankouski.boarditfront.dto.BoardGameDTO;
import pl.sankouski.boarditfront.dto.BoardGameSummaryDto;
import pl.sankouski.boarditfront.dto.BoardGameUpdateSummaryDTO;
import pl.sankouski.boarditfront.mapper.BoardGameMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest
class BoardGameServiceTest {

    private MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
    private RestClient.Builder builder = RestClient.builder();
    private BoardGameService boardGameService;

    @BeforeEach
    void setUp() {
        customizer.customize(builder);
        boardGameService = new BoardGameService(builder.build());
        boardGameService.setToken("test-token");
    }

    @Test
    void testGetAllBoardGames() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames";
        String mockResponse = """
                [
                    {"id": 1, "name": "Game1"},
                    {"id": 2, "name": "Game2"}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<BoardGameDTO> result = boardGameService.getAllBoardGames();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Game1");
        assertThat(result.get(1).getName()).isEqualTo("Game2");
    }

    @Test
    void testGetBoardGameById() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/1";
        String mockResponse = """
                {"id": 1, "name": "Game1"}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        BoardGameDTO result = boardGameService.getBoardGameById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Game1");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testCreateBoardGame() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames";
        BoardGameDTO newGame = new BoardGameDTO(123L, "NewGame");
        String mockResponse = """
                {"id": 123, "name": "NewGame"}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        BoardGameDTO result = boardGameService.createBoardGame(newGame);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("NewGame");
        assertThat(result.getId()).isEqualTo(123L);
    }

    @Test
    void testUpdateBoardGame() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/1";
        BoardGameUpdateSummaryDTO updateGame = new BoardGameUpdateSummaryDTO(1L, "UpdatedGame");
        String mockResponse = """
                {"id": 1, "name": "UpdatedGame"}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        BoardGameUpdateSummaryDTO result = boardGameService.updateBoardGame(1L, updateGame);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("UpdatedGame");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testDeleteBoardGame() {
        String apiUrl = "http://localhost:8080/boardgames/1";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        boardGameService.deleteBoardGame(1L);

        assertThat(true).isTrue();
    }

    @Test
    void testGetAllBoardGamesSummary() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames";
        String mockResponse = """
                [
                    {"id": 1, "name": "Game1", "imageLink": "url1"},
                    {"id": 2, "name": "Game2", "imageLink": "url2"}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<BoardGameSummaryDto> result = boardGameService.getAllBoardGamesSummary();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Game1");
        assertThat(result.get(1).getName()).isEqualTo("Game2");
    }

    @Test
    void testAddCategoryToBoardGame() {
        String apiUrl = "http://localhost:8080/boardgames/1/categories/2";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        boardGameService.addCategoryToBoardGame(1L, 2L);

        assertThat(true).isTrue();
    }

    @Test
    void testRemoveCategoryFromBoardGame() {
        String apiUrl = "http://localhost:8080/boardgames/1/categories/2";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        boardGameService.removeCategoryFromBoardGame(1L, 2L);

        assertThat(true).isTrue();
    }

    @Test
    void testAddMechanicToBoardGame() {
        String apiUrl = "http://localhost:8080/boardgames/1/mechanics/3";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        boardGameService.addMechanicToBoardGame(1L, 3L);

        assertThat(true).isTrue();
    }

    @Test
    void testRemoveMechanicFromBoardGame() {
        String apiUrl = "http://localhost:8080/boardgames/1/mechanics/3";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        boardGameService.removeMechanicFromBoardGame(1L, 3L);

        assertThat(true).isTrue();
    }
}