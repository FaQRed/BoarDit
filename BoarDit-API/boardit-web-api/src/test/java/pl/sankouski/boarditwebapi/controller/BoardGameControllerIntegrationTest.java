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
import pl.sankouski.boarditdata.dto.BoardGameDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/boardGameControllerTest/boardgame-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BoardGameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private BoardGameDTO boardGameDTO;

    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzQGdtYWlsLmNvbSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTczNzIzNzk4NCwiZXhwIjo2MDAwMDE3MzcyMzc5ODR9.0AxCj2fKfYhFPfKkfSP47L-N-AD65sFiTE_SsD-xWVo";

    @BeforeEach
    public void setUp() {
        boardGameDTO = new BoardGameDTO();
        boardGameDTO.setName("New Board Game");
        boardGameDTO.setYearPublished(2021);
        boardGameDTO.setMinPlayers(2);
        boardGameDTO.setMaxPlayers(4);
        boardGameDTO.setPlayingTime(60);
    }

    private MockHttpServletRequestBuilder authenticatedRequest(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header("Authorization", AUTH_TOKEN);
    }

    @Test
    public void shouldGetAllBoardGames() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/boardgames"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test Board Game 1"))
                .andExpect(jsonPath("$[1].name").value("Test Board Game 2"));
    }

    @Test
    public void shouldGetBoardGameById() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/boardgames/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Board Game 1"));
    }

    @Test
    public void shouldReturnNotFoundForNonExistentBoardGame() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/boardgames/999"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Board game with ID 999 not found."));
    }

    @Test
    public void shouldCreateBoardGame() throws Exception {
        String json = """
                {
                    "name": "New Board Game",
                    "descriptiom": "New Board Game Description",
                    "yearPublished": 2021,
                    "minPlayers": 2,
                    "maxPlayers": 4,
                    "playingTime": 60,
                    "imageLink" : "ggferfe.jpg",
                    "categories": [1, 2],
                    "mechanics": [1, 2],
                    "artists": ["koko jambo", "mama dawaj"]
                }
                """;

        mockMvc.perform(authenticatedRequest(post("/boardgames"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Board Game"));
    }

    @Test
    public void shouldUpdateBoardGame() throws Exception {
        String json = """
                {
                    "name": "Updated Board Game",
                    "yearPublished": 2022,
                    "minPlayers": 3,
                    "maxPlayers": 5,
                    "playingTime": 90
                }
                """;

        mockMvc.perform(authenticatedRequest(put("/boardgames/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Board Game"));
    }

    @Test
    public void shouldDeleteBoardGame() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/boardgames/1")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldAddCategoryToBoardGame() throws Exception {
        mockMvc.perform(authenticatedRequest(post("/boardgames/1/categories/3")))
                .andExpect(status().isOk())
                .andExpect(content().string("Category added successfully."));
    }

    @Test
    public void shouldRemoveCategoryFromBoardGame() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/boardgames/1/categories/1")))
                .andExpect(status().isOk())
                .andExpect(content().string("Category removed successfully."));
    }

    @Test
    public void shouldAddMechanicToBoardGame() throws Exception {
        mockMvc.perform(authenticatedRequest(post("/boardgames/1/mechanics/3")))
                .andExpect(status().isOk())
                .andExpect(content().string("Mechanic added successfully."));
    }

    @Test
    public void shouldRemoveMechanicFromBoardGame() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/boardgames/1/mechanics/1")))
                .andExpect(status().isOk())
                .andExpect(content().string("Mechanic removed successfully."));
    }
}