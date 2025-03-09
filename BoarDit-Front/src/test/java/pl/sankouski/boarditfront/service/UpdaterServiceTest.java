package pl.sankouski.boarditfront.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RestClientTest
class UpdaterServiceTest {

    private MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
    private RestClient.Builder builder = RestClient.builder();
    private UpdaterService updaterService;

    @BeforeEach
    void setUp() {
        customizer.customize(builder);
        updaterService = new UpdaterService(builder.build());
        updaterService.setToken("test-token");
    }

    @Test
    void testUpdateBoardGameSuccess() {
        String apiUrl = "http://localhost:8080/admin/updater/start?number=42";
        String mockResponse = "Update successful";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andExpect(MockRestRequestMatchers.header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.TEXT_PLAIN));

        String result = updaterService.updateBoardGame(42);

        assertThat(result).isEqualTo(mockResponse);
    }

    @Test
    void testUpdateBoardGameClientError() {
        String apiUrl = "http://localhost:8080/admin/updater/start?number=42";
        String errorResponse = "Invalid request";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andExpect(MockRestRequestMatchers.header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(MockRestResponseCreators.withBadRequest().body(errorResponse));

        assertThatThrownBy(() -> updaterService.updateBoardGame(42))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid request: " + errorResponse);
    }

    @Test
    void testUpdateBoardGameNotFound() {
        String apiUrl = "http://localhost:8080/admin/updater/start?number=42";
        String errorResponse = "Game not found";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andExpect(MockRestRequestMatchers.header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND).body(errorResponse));

        assertThatThrownBy(() -> updaterService.updateBoardGame(42))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Game not found: " + errorResponse);
    }

    @Test
    void testUpdateBoardGameServerError() {
        String apiUrl = "http://localhost:8080/admin/updater/start?number=42";
        String errorResponse = "Internal server error";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andExpect(MockRestRequestMatchers.header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(MockRestResponseCreators.withServerError().body(errorResponse));

        assertThatThrownBy(() -> updaterService.updateBoardGame(42))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Server error: " + errorResponse);
    }
}