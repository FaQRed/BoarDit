package pl.sankouski.boarditwebapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.sankouski.boarditdata.dto.MechanicDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/mechanicControllerTest/mechanic-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MechanicControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private MechanicDTO mechanicDTO;

    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzQGdtYWlsLmNvbSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTczNzIzNzk4NCwiZXhwIjo2MDAwMDE3MzcyMzc5ODR9.0AxCj2fKfYhFPfKkfSP47L-N-AD65sFiTE_SsD-xWVo";

    @BeforeEach
    public void setUp() {
        mechanicDTO = new MechanicDTO();
        mechanicDTO.setName("New Mechanic");
    }

    private MockHttpServletRequestBuilder authenticatedRequest(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header("Authorization", AUTH_TOKEN);
    }

    @Test
    public void shouldGetAllMechanics() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/mechanics"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Mechanic 1"))
                .andExpect(jsonPath("$[1].name").value("Mechanic 2"));
    }

    @Test
    public void shouldGetMechanicById() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/mechanics/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mechanic 1"));
    }

    @Test
    public void shouldReturnNotFoundForNonExistentMechanic() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/mechanics/999"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateMechanic() throws Exception {
        String json = """
                {
                    "name": "New Mechanic"
                }
                """;

        mockMvc.perform(authenticatedRequest(post("/mechanics"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Mechanic"));
    }

    @Test
    public void shouldUpdateMechanic() throws Exception {
        String json = """
                {
                    "name": "Updated Mechanic"
                }
                """;

        mockMvc.perform(authenticatedRequest(put("/mechanics/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Mechanic"));
    }

    @Test
    public void shouldDeleteMechanic() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/mechanics/1")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentMechanic() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/mechanics/999")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetMechanicsByIds() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/mechanics/list/1,2"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Mechanic 1"))
                .andExpect(jsonPath("$[1].name").value("Mechanic 2"));
    }
}