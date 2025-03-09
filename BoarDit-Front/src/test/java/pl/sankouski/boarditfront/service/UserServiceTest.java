package pl.sankouski.boarditfront.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestClient;
import pl.sankouski.boarditfront.model.user.Role;
import pl.sankouski.boarditfront.model.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RestClientTest
class UserServiceTest {

    private MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
    private RestClient.Builder builder = RestClient.builder();
    private UserService userService;

    @BeforeEach
    void setUp() {
        customizer.customize(builder);
        userService = new UserService(builder.build());
        userService.setToken("test-token");
    }

    @Test
    void testGetAllRoles() {
        String apiUrl = "http://localhost:8080/users/roles";
        String mockResponse = """
                [
                    {"pid": "ROLE_ADMIN"},
                    {"pid": "ROLE_USER"}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andExpect(MockRestRequestMatchers.header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<Role> roles = userService.getAllRoles();

        assertThat(roles).isNotNull();
        assertThat(roles).hasSize(2);
        assertThat(roles.get(0).getPid()).isEqualTo("ROLE_ADMIN");
        assertThat(roles.get(1).getPid()).isEqualTo("ROLE_USER");
    }

    @Test
    void testGetAllUsers() {
        String apiUrl = "http://localhost:8080/users";
        String mockResponse = """
                [
                    {"id": 1, "login": "user1"},
                    {"id": 2, "login": "user2"}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andExpect(MockRestRequestMatchers.header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<User> users = userService.getAllUsers();

        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getLogin()).isEqualTo("user1");
        assertThat(users.get(1).getLogin()).isEqualTo("user2");
    }

    @Test
    void testGetUserByUsername() {
        String username = "user1";
        String apiUrl = "http://localhost:8080/users/username/" + username;
        String mockResponse = """
                {"pid": 1, "login": "user1"}
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andExpect(MockRestRequestMatchers.header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        User user = userService.getUserByUsername(username);

        assertThat(user).isNotNull();
        assertThat(user.getLogin()).isEqualTo("user1");
        assertThat(user.getPid()).isEqualTo(1L);
    }

    @Test
    void testSaveUser() {
        String apiUrl = "http://localhost:8080/users/saveUser";
        User user = new User();
        user.setPid(1L);
        user.setLogin("user1");

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andExpect(MockRestRequestMatchers.header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(MockRestResponseCreators.withSuccess());

        userService.saveUser(user);

        assertThat(true).isTrue(); // Убедиться, что метод завершился без исключений.
    }

    @Test
    void testUpdateUser() {
        String apiUrl = "http://localhost:8080/users/updateUser";
        User user = new User();
        user.setPid(1L);
        user.setLogin("user1");

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andExpect(MockRestRequestMatchers.header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(MockRestResponseCreators.withSuccess());

        userService.updateUser(user);

        assertThat(true).isTrue(); // Убедиться, что метод завершился без исключений.
    }

    @Test
    void testDeleteUserById() {
        Long userId = 1L;
        String apiUrl = "http://localhost:8080/users/delete/" + userId;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andExpect(MockRestRequestMatchers.header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(MockRestResponseCreators.withSuccess());

        userService.deleteUserById(userId);

        assertThat(true).isTrue(); // Убедиться, что метод завершился без исключений.
    }

    @Test
    void testFilterUser() {
        String filterText = "admin";
        String apiUrl = "http://localhost:8080/users/filter?filterText=" + filterText;
        String mockResponse = """
                [
                    {"id": 1, "login": "admin"}
                ]
                """;

        customizer.getServer()
                .expect(MockRestRequestMatchers.requestTo(apiUrl))
                .andExpect(MockRestRequestMatchers.header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
                .andRespond(MockRestResponseCreators.withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        List<User> users = userService.filterUser(filterText);

        assertThat(users).isNotNull();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getLogin()).isEqualTo("admin");
    }
}