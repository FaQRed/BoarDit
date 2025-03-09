package pl.sankouski.boarditfront.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import pl.sankouski.boarditfront.model.user.Role;
import pl.sankouski.boarditfront.model.user.User;
import pl.sankouski.boarditfront.security.JwtUtils;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private final RestClient restClient;
    private final String apiUrl = "http://localhost:8080/users";
    private String token;

    public UserService(RestClient restClient) {
        this.restClient = restClient;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private RequestHeadersSpec<?> addAuthorization(RequestHeadersSpec<?> requestSpec) {
        if (token != null && !token.isEmpty()) {
            return requestSpec.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
        return requestSpec;
    }

    public List<Role> getAllRoles() {
        try {
            List<Role> roles = addAuthorization(
                    restClient.get().uri(apiUrl + "/roles")
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<Role>>() {
                    });
            if (roles == null || roles.isEmpty()) {
                throw new RuntimeException("No roles found.");
            }
            return roles;
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public List<User> getAllUsers() {
        try {
            List<User> users = addAuthorization(
                    restClient.get().uri(apiUrl)
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<User>>() {
                    });
            if (users == null || users.isEmpty()) {
                throw new RuntimeException("No users found.");
            }
            return users;
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public User getUserByUsername(String username) {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/username/" + username)
            ).retrieve()
                    .body(User.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public User getUserById(Long id) {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/" + id)
            ).retrieve()
                    .body(User.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public void saveUser(User user) {
        try {
            addAuthorization(
                    restClient.post()
                            .uri(apiUrl + "/saveUser")
                            .body(user)
            ).retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
    }

    public void updateUser(User user) {
        try {
            addAuthorization(
                    restClient.put()
                            .uri(apiUrl + "/updateUser")
                            .body(user)
            ).retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
    }

    public void deleteUserById(Long id){
        try {
            addAuthorization(
                    restClient.delete().uri(apiUrl + "/delete/" + id)
            ).retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
    }


    public User getCurrentUserFromToken(String token) {
        try {
            Map<String, Object> claims = JwtUtils.decodeJwt(token); // Предполагается наличие `JwtUtils`
            String username = (String) claims.get("sub");
            return getUserByUsername(username);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to decode JWT or fetch user: " + e.getMessage());
        }
    }

    public List<User> filterUser(String filterText) {
        try {
            if (filterText == null || filterText.isEmpty()) {
                return getAllUsers();
            }
            List<User> users = addAuthorization(
                    restClient.get().uri(apiUrl + "/filter?filterText=" + filterText)
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<User>>() {
                    });
            if (users == null || users.isEmpty()) {
                throw new RuntimeException("No users match the filter text.");
            }
            return users;
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    private void handleClientError(HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new RuntimeException("Invalid data provided: " + e.getResponseBodyAsString());
        } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new RuntimeException("Resource not found: " + e.getResponseBodyAsString());
        } else if (e.getStatusCode() == HttpStatus.CONFLICT) {
            throw new RuntimeException("Conflict error: " + e.getResponseBodyAsString());
        } else {
            throw new RuntimeException("Client error: " + e.getResponseBodyAsString());
        }
    }

    private void handleServerError(HttpServerErrorException e) {
        throw new RuntimeException("Server error: " + e.getResponseBodyAsString());
    }
}