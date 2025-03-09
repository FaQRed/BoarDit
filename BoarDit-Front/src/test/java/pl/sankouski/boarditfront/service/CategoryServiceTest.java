package pl.sankouski.boarditfront.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestClient;
import pl.sankouski.boarditfront.dto.CategoryDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest
class CategoryServiceTest {

    private MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
    private RestClient.Builder builder = RestClient.builder();
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        customizer.customize(builder);
        categoryService = new CategoryService(builder.build());
        categoryService.setToken("test-token");
    }

    @Test
    void testGetAllCategories() throws Exception {
        String apiUrl = "http://localhost:8080/categories";
        String mockResponse = """
                [
                    {"id": 1, "name": "Category1"},
                    {"id": 2, "name": "Category2"}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Category1");
        assertThat(result.get(1).getName()).isEqualTo("Category2");
    }

    @Test
    void testCreateCategory() throws Exception {
        String apiUrl = "http://localhost:8080/categories";
        CategoryDTO newCategory = new CategoryDTO(123L,"NewCategory");
        String mockResponse = """
                {"id": 123, "name": "NewCategory"}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        CategoryDTO result = categoryService.createCategory(newCategory);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("NewCategory");
        assertThat(result.getId()).isEqualTo(123L);
    }

    @Test
    void testDeleteCategory() {
        String apiUrl = "http://localhost:8080/categories/1";

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        categoryService.deleteCategory(1L);

        assertThat(true).isTrue();
    }

    @Test
    void testUpdateCategory() throws Exception {
        String apiUrl = "http://localhost:8080/categories/1";
        CategoryDTO updatedCategory = new CategoryDTO(1L, "UpdatedCategory");

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess());

        categoryService.updateCategory(updatedCategory, 1L);

        assertThat(true).isTrue();
    }

    @Test
    void testGetCategoriesByIds() throws Exception {
        String apiUrl = "http://localhost:8080/categories/list/1,2";
        String mockResponse = """
                [
                    {"id": 1, "name": "Category1"},
                    {"id": 2, "name": "Category2"}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<CategoryDTO> result = categoryService.getCategoriesByIds(List.of(1L, 2L));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Category1");
        assertThat(result.get(1).getName()).isEqualTo("Category2");
    }
}