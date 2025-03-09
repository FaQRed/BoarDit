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
import pl.sankouski.boarditdata.dto.CategoryDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/categoryControllerTest/category-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzQGdtYWlsLmNvbSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTczNzIzNzk4NCwiZXhwIjo2MDAwMDE3MzcyMzc5ODR9.0AxCj2fKfYhFPfKkfSP47L-N-AD65sFiTE_SsD-xWVo";

    private CategoryDTO categoryDTO;

    @BeforeEach
    public void setUp() {
        categoryDTO = new CategoryDTO();
        categoryDTO.setName("New Category");
    }

    private MockHttpServletRequestBuilder authenticatedRequest(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header("Authorization", AUTH_TOKEN);
    }

    @Test
    public void shouldGetAllCategories() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/categories"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[1].name").value("Category 2"))
                .andExpect(jsonPath("$[2].name").value("Category 3"));
    }

    @Test
    public void shouldCreateCategory() throws Exception {
        String json = """
                {
                    "name": "New Category"
                }
                """;

        mockMvc.perform(authenticatedRequest(post("/categories"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Category"));
    }

    @Test
    public void shouldDeleteCategory() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/categories/1")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentCategory() throws Exception {
        mockMvc.perform(authenticatedRequest(delete("/categories/999")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Category with ID 999 not found."));
    }

    @Test
    public void shouldGetCategoriesByIds() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/categories/list/1,2"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[1].name").value("Category 2"));
    }

    @Test
    public void shouldReturnNotFoundForEmptyCategoryList() throws Exception {
        mockMvc.perform(authenticatedRequest(get("/categories/list/999"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateCategory() throws Exception {
        String json = """
                {
                    "name": "Updated Category"
                }
                """;

        mockMvc.perform(authenticatedRequest(put("/categories/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("\"OK\""));
    }

    @Test
    public void shouldReturnBadRequestForUpdatingNonExistentCategory() throws Exception {
        String json = """
                {
                    "name": "Nonexistent Category"
                }
                """;

        mockMvc.perform(authenticatedRequest(put("/categories/999"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Category not found with ID: 999"));
    }
}