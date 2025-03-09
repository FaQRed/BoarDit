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
import pl.sankouski.boarditdata.dto.ArtistDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/artistControllerTest/artist-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ArtistControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ArtistDTO artistDTO;

    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzQGdtYWlsLmNvbSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTczNzIzNzk4NCwiZXhwIjo2MDAwMDE3MzcyMzc5ODR9.0AxCj2fKfYhFPfKkfSP47L-N-AD65sFiTE_SsD-xWVo";

    @BeforeEach
    public void setUp() {
        artistDTO = new ArtistDTO();
        artistDTO.setName("New Artist");
    }

    private MockHttpServletRequestBuilder authenticatedRequest(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header("Authorization", AUTH_TOKEN);
    }

    @Test
    public void shouldGetAllArtists() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/artists"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test Artist 1"))
                .andExpect(jsonPath("$[1].name").value("Test Artist 2"));
    }

    @Test
    public void shouldGetArtistById() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/artists/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Artist 1"));
    }

    @Test
    public void shouldReturnNotFoundForNonExistentArtist() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/artists/999"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateArtist() throws Exception {
        String json = """
                {
                    "name": "New Artist"
                }
                """;

        mockMvc.perform(authenticatedRequest(post("/artists"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Artist"));
    }

    @Test
    public void shouldUpdateArtist() throws Exception {
        String json = """
                {
                    "name": "Updated Artist"
                }
                """;

        mockMvc.perform(authenticatedRequest(put("/artists/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Artist"));
    }

    @Test
    public void shouldDeleteArtist() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/artists/1")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentArtist() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/artists/999")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetArtistsByNames() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/artists/names")
                        .param("names", "Test Artist 1", "Test Artist 2")
                        .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test Artist 1"))
                .andExpect(jsonPath("$[1].name").value("Test Artist 2"));
    }
}