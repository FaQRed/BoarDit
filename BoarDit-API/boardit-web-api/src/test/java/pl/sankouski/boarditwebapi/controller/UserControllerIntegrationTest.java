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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = "/userControllerTest/user-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzQGdtYWlsLmNvbSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTczNzIzNzk4NCwiZXhwIjo2MDAwMDE3MzcyMzc5ODR9.0AxCj2fKfYhFPfKkfSP47L-N-AD65sFiTE_SsD-xWVo";

    private String userJson(String login, String password, String firstName, String lastName, String role) {
        return """
            {
                "login": "%s",
                "password": "%s",
                "firstName": "%s",
                "lastName": "%s",
                "roles": [{"pid": "%s"}]
            }
            """.formatted(login, password, firstName, lastName, role);
    }

    private String authenticatedHeader() {
        return AUTH_TOKEN;
    }

    @Test
    public void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", authenticatedHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[1].login").value("admin@gmail.com"))
                .andExpect(jsonPath("$[2].login").value("user@gmail.com"));
    }

    @Test
    public void shouldGetUserById() throws Exception {
        mockMvc.perform(get("/users/2")
                        .header("Authorization", authenticatedHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pid").value(2))
                .andExpect(jsonPath("$.login").value("admin@gmail.com"));
    }

    @Test
    public void shouldReturnNotFoundForNonExistentUser() throws Exception {
        mockMvc.perform(get("/users/999")
                        .header("Authorization", authenticatedHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldSaveUser() throws Exception {
        String json = """
        {
            "login": "12312312312",
            "password": "11111",
            "firstName": "Kiryl",
            "lastName": "Sankouski",
            "middleName": "And",
            "roles": [
                        { "pid": "ROLE_USER" }
                      ],
            "status": "ACTIVE"
        }
        """;

        mockMvc.perform(post("/users/saveUser")
                        .header("Authorization", authenticatedHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("User successfully added"));
    }

    @Test
    public void shouldNotSaveUserIfLoginExists() throws Exception {
        String json = userJson("admin@gmail.com", "password123", "Admin", "User", "ROLE_ADMIN");

        mockMvc.perform(post("/users/saveUser")
                        .header("Authorization", authenticatedHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(content().string("A user with this login already exists"));
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        String json = """
        {
            "pid": 2,
            "login": "12312312312",
            "password": "11111",
            "firstName": "Kiryl",
            "lastName": "Sankouski",
            "middleName": "And",
            "roles": [
                        { "pid": "ROLE_USER" }
                      ],
            "status": "ACTIVE"
        }
        """;
        mockMvc.perform(put("/users/updateUser")
                        .header("Authorization", authenticatedHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully updated"));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/delete/1")
                        .header("Authorization", authenticatedHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully deleted"));
    }

    @Test
    public void shouldFilterUsers() throws Exception {
        mockMvc.perform(get("/users/filter")
                        .param("filterText", "admin")
                        .header("Authorization", authenticatedHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].login").value("admin@gmail.com"));
    }

    @Test
    public void shouldGetUserByUsername() throws Exception {
        mockMvc.perform(get("/users/username/admin@gmail.com")
                        .header("Authorization", authenticatedHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("admin@gmail.com"));
    }
}