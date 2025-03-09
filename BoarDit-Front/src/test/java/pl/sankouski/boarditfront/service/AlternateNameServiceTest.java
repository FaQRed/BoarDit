package pl.sankouski.boarditfront.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestClient;
import pl.sankouski.boarditfront.dto.AlternateNameDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest
class AlternateNameServiceTest {

    private MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
    private RestClient.Builder builder = RestClient.builder();
    private AlternateNameService alternateNameService;

    @BeforeEach
    public void setUp() {
        customizer.customize(builder);
        alternateNameService = new AlternateNameService(builder.build());
        alternateNameService.setToken("test-token");
    }

    @Test
    void testGetAllAlternateNamesByGameId() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/1/alternatenames";
        String mockResponse = """
                [
                    {"name": "AltName1", "id": 1},
                    {"name": "AltName2", "id": 2}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<AlternateNameDTO> result = alternateNameService.getAllAlternateNamesByGameId(1L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("AltName1");
        assertThat(result.get(1).getName()).isEqualTo("AltName2");
    }

    @Test
    void testGetAlternateNameById() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/1/alternatenames/10";
        String mockResponse = """
                {"name": "AltName1", "id": 10}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        AlternateNameDTO result = alternateNameService.getAlternateNameById(1L, 10L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("AltName1");
        assertThat(result.getId()).isEqualTo(10);
    }

    @Test
    void testCreateAlternateName() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/1/alternatenames";
        AlternateNameDTO newAlternateName = new AlternateNameDTO("NewAltName", 123L);

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        alternateNameService.createAlternateName(1L, newAlternateName);
        assertThat(true).isTrue();
    }

    @Test
    void testUpdateAlternateName() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/1/alternatenames/123";
        AlternateNameDTO updatedAlternateName = new AlternateNameDTO("UpdatedAltName", 123L);

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        alternateNameService.updateAlternateName(1L, 123L, updatedAlternateName);

        assertThat(true).isTrue();
    }

    @Test
    void testDeleteAlternateName() throws Exception {
        String apiUrl = "http://localhost:8080/boardgames/1/alternatenames/123";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        alternateNameService.deleteAlternateName(1L, 123L);

        assertThat(true).isTrue();
    }
}