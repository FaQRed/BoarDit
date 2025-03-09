package pl.sankouski.boarditfront.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestClient;
import pl.sankouski.boarditfront.dto.MechanicDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest
class MechanicServiceTest {

    private MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
    private RestClient.Builder builder = RestClient.builder();
    private MechanicService mechanicService;

    @BeforeEach
    void setUp() {
        customizer.customize(builder);
        mechanicService = new MechanicService(builder.build());
        mechanicService.setToken("test-token");
    }

    @Test
    void testGetAllMechanics() throws Exception {
        String apiUrl = "http://localhost:8080/mechanics";
        String mockResponse = """
                [
                    {"id": 1, "name": "Mechanic1"},
                    {"id": 2, "name": "Mechanic2"}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<MechanicDTO> result = mechanicService.getAllMechanics();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Mechanic1");
        assertThat(result.get(1).getName()).isEqualTo("Mechanic2");
    }

    @Test
    void testGetMechanicById() throws Exception {
        String apiUrl = "http://localhost:8080/mechanics/1";
        String mockResponse = """
                {"id": 1, "name": "Mechanic1"}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        MechanicDTO result = mechanicService.getMechanicById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Mechanic1");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testCreateMechanic() throws Exception {
        String apiUrl = "http://localhost:8080/mechanics";
        MechanicDTO newMechanic = new MechanicDTO(123L, "NewMechanic");
        String mockResponse = """
                {"id": 123, "name": "NewMechanic"}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        MechanicDTO result = mechanicService.createMechanic(newMechanic);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("NewMechanic");
        assertThat(result.getId()).isEqualTo(123L);
    }

    @Test
    void testUpdateMechanic() throws Exception {
        String apiUrl = "http://localhost:8080/mechanics/1";
        MechanicDTO updatedMechanic = new MechanicDTO(1L, "UpdatedMechanic");
        String mockResponse = """
                {"id": 1, "name": "UpdatedMechanic"}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        MechanicDTO result = mechanicService.updateMechanic(1L, updatedMechanic);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("UpdatedMechanic");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testDeleteMechanic() {
        String apiUrl = "http://localhost:8080/mechanics/1";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        mechanicService.deleteMechanic(1L);

        assertThat(true).isTrue();
    }

    @Test
    void testGetMechanicsByIds() throws Exception {
        String apiUrl = "http://localhost:8080/mechanics/list/1,2";
        String mockResponse = """
                [
                    {"id": 1, "name": "Mechanic1"},
                    {"id": 2, "name": "Mechanic2"}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<MechanicDTO> result = mechanicService.getMechanicsByIds(List.of(1L, 2L));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Mechanic1");
        assertThat(result.get(1).getName()).isEqualTo("Mechanic2");
    }
}