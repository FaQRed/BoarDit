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
import pl.sankouski.boarditfront.dto.ExpansionDTO;


import java.util.List;

@Service
public class ExpansionService {

    @Autowired
    private final RestClient restClient;
    private final String apiUrl = "http://localhost:8080/boardgames";
    private String token;

    public ExpansionService(RestClient restClient) {
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

    public List<ExpansionDTO> getAllExpansions() {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/expansions")
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<ExpansionDTO>>() {});
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public List<ExpansionDTO> getExpansionsByBoardGameId(Long boardGameId) {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/" + boardGameId + "/expansions")
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<ExpansionDTO>>() {});
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public ExpansionDTO getExpansionById(Long id) {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/expansions/" + id)
            ).retrieve()
                    .body(ExpansionDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public ExpansionDTO createExpansion(Long boardGameId, ExpansionDTO expansionDTO) {
        try {
            return addAuthorization(
                    restClient.post()
                            .uri(apiUrl + "/" + boardGameId + "/expansions")
                            .body(expansionDTO)
            ).retrieve()
                    .body(ExpansionDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public ExpansionDTO updateExpansion(Long boardGameId, Long id, ExpansionDTO expansionDTO) {
        try {
            return addAuthorization(
                    restClient.put()
                            .uri(apiUrl + "/" + boardGameId + "/expansions/" + id)
                            .body(expansionDTO)
            ).retrieve()
                    .body(ExpansionDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public void deleteExpansion(Long id) {
        try {
            addAuthorization(
                    restClient.delete()
                            .uri(apiUrl + "/expansions/" + id)
            ).retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
    }

    private void handleClientError(HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new RuntimeException("Invalid request: " + e.getResponseBodyAsString());
        } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new RuntimeException("Resource not found: " + e.getResponseBodyAsString());
        } else {
            throw new RuntimeException("Client error: " + e.getResponseBodyAsString());
        }
    }

    private void handleServerError(HttpServerErrorException e) {
        throw new RuntimeException("Server error: " + e.getResponseBodyAsString());
    }
}