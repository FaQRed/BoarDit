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
import pl.sankouski.boarditdata.dto.AlternateNameDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/altNameControllerTest/alt_name-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AlternateNameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private AlternateNameDTO alternateNameDTO;

    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzQGdtYWlsLmNvbSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTczNzIzNzk4NCwiZXhwIjo2MDAwMDE3MzcyMzc5ODR9.0AxCj2fKfYhFPfKkfSP47L-N-AD65sFiTE_SsD-xWVo";

    @BeforeEach
    public void setUp() {
        alternateNameDTO = new AlternateNameDTO();
        alternateNameDTO.setName("New Alternate Name");
    }

    private MockHttpServletRequestBuilder authenticatedRequest(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header("Authorization", AUTH_TOKEN);
    }

    @Test
    public void shouldGetAllAlternateNamesByGameId() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/boardgames/1/alternatenames"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test Alternate Name 1"))
                .andExpect(jsonPath("$[1].name").value("Test Alternate Name 2"));
    }

    @Test
    public void shouldGetAlternateNameById() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/boardgames/1/alternatenames/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Alternate Name 1"));
    }

    @Test
    public void shouldReturnNotFoundForNonExistentAlternateName() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/boardgames/1/alternatenames/999"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void shouldCreateAlternateName() throws Exception {
        String json = """
                {
                    "name": "Alternate Game Name"
                }
                """;

        mockMvc.perform(authenticatedRequest(post("/boardgames/1/alternatenames"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("Alternate name created successfully."));
    }

    @Test
    public void shouldUpdateAlternateName() throws Exception {
        String json = """
                {
                    "id": 1,
                    "name": "Updated Alternate Name"
                }
                """;

        mockMvc.perform(authenticatedRequest(put("/boardgames/1/alternatenames/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteAlternateName() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/boardgames/1/alternatenames/1")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentAlternateName() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/boardgames/1/alternatenames/999")))
                .andExpect(status().is4xxClientError());
    }
}