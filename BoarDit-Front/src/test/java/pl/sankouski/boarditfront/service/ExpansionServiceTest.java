package pl.sankouski.boarditfront.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestClient;
import pl.sankouski.boarditfront.dto.ExpansionDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest
class ExpansionServiceTest {

    private MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
    private RestClient.Builder builder = RestClient.builder();
    private ExpansionService expansionService;

    @BeforeEach
    void setUp() {
        customizer.customize(builder);
        expansionService = new ExpansionService(builder.build());
        expansionService.setToken("test-token");
    }

    @Test
    void testGetAllExpansions() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/expansions";
        String mockResponse = """
                [
                    {"id": 1, "name": "Expansion1"},
                    {"id": 2, "name": "Expansion2"}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<ExpansionDTO> result = expansionService.getAllExpansions();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Expansion1");
        assertThat(result.get(1).getName()).isEqualTo("Expansion2");
    }

    @Test
    void testGetExpansionsByBoardGameId() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/1/expansions";
        String mockResponse = """
                [
                    {"id": 1, "name": "Expansion1"},
                    {"id": 2, "name": "Expansion2"}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<ExpansionDTO> result = expansionService.getExpansionsByBoardGameId(1L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Expansion1");
        assertThat(result.get(1).getName()).isEqualTo("Expansion2");
    }

    @Test
    void testGetExpansionById() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/expansions/1";
        String mockResponse = """
                {"id": 1, "name": "Expansion1"}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        ExpansionDTO result = expansionService.getExpansionById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Expansion1");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testCreateExpansion() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/1/expansions";
        ExpansionDTO newExpansion = new ExpansionDTO(123L, "NewExpansion");
        String mockResponse = """
                {"id": 123, "name": "NewExpansion"}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        ExpansionDTO result = expansionService.createExpansion(1L, newExpansion);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("NewExpansion");
        assertThat(result.getId()).isEqualTo(123L);
    }

    @Test
    void testUpdateExpansion() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/1/expansions/123";
        ExpansionDTO updatedExpansion = new ExpansionDTO(123L, "UpdatedExpansion");
        String mockResponse = """
                {"id": 123, "name": "UpdatedExpansion"}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        ExpansionDTO result = expansionService.updateExpansion(1L, 123L, updatedExpansion);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("UpdatedExpansion");
        assertThat(result.getId()).isEqualTo(123L);
    }

    @Test
    void testDeleteExpansion() {
        String apiUrl = "http://localhost:8080/boardgames/expansions/123";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        expansionService.deleteExpansion(123L);

        assertThat(true).isTrue();
    }
}