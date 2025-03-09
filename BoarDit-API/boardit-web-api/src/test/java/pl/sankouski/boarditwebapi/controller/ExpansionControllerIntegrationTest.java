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
import pl.sankouski.boarditdata.dto.ExpansionDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/expansionControllerTest/expansion-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ExpansionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzQGdtYWlsLmNvbSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTczNzIzNzk4NCwiZXhwIjo2MDAwMDE3MzcyMzc5ODR9.0AxCj2fKfYhFPfKkfSP47L-N-AD65sFiTE_SsD-xWVo";

    private MockHttpServletRequestBuilder authenticatedRequest(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header("Authorization", AUTH_TOKEN);
    }

    @Test
    public void shouldGetAllExpansions() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/boardgames/expansions"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Expansion 1"))
                .andExpect(jsonPath("$[1].name").value("Expansion 2"));
    }


    @Test
    public void shouldGetAllExpansionsByBoardGameId() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/boardgames/1/expansions"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Expansion 1"));
    }

    @Test
    public void shouldReturnEmptyListForNonExistentBoardGameId() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/boardgames/999/expansions"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void shouldGetExpansionById() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/boardgames/expansions/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Expansion 1"));
    }

    @Test
    public void shouldReturnNotFoundForInvalidExpansionId() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/boardgames/expansions/999"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateExpansion() throws Exception {
        String json = """
                {
                    "name": "New Expansion",
                    "description": "New Description",
                    "yearPublished": 2021,
                    "minPlayers": 2,
                    "maxPlayers": 4,
                    "playingTime": 60
                }
                """;

        mockMvc.perform(authenticatedRequest(post("/boardgames/1/expansions"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Expansion"));
    }

    @Test
    public void shouldReturnBadRequestForInvalidCreateExpansionRequest() throws Exception {
        String json = """
                {
                    "description": "Missing Name"
                }
                """;

        mockMvc.perform(authenticatedRequest(post("/boardgames/1/expansions"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateExpansion() throws Exception {
        String json = """
                {
                    "name": "Updated Expansion",
                    "description": "Updated Description",
                    "yearPublished": 2022,
                    "minPlayers": 3,
                    "maxPlayers": 5,
                    "playingTime": 90
                }
                """;

        mockMvc.perform(authenticatedRequest(put("/boardgames/1/expansions/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Expansion"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    public void shouldDeleteExpansion() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/boardgames/expansions/1")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentExpansion() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/boardgames/expansions/999")))
                .andExpect(status().isNotFound());
    }
}