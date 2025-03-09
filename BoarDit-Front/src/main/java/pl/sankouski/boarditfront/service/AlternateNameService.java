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
import pl.sankouski.boarditfront.dto.AlternateNameDTO;

import java.util.List;

@Service
public class AlternateNameService {

    @Autowired
    private final RestClient restClient;

    private final String apiUrl = "http://localhost:8080/boardgames";
    private String token;

    public AlternateNameService(RestClient restClient) {
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

    public List<AlternateNameDTO> getAllAlternateNamesByGameId(Long boardGameId) {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/" + boardGameId + "/alternatenames")
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<AlternateNameDTO>>() {});
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public AlternateNameDTO getAlternateNameById(Long boardGameId, Long id) {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/" + boardGameId + "/alternatenames/" + id)
            ).retrieve()
                    .body(AlternateNameDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public void createAlternateName(Long boardGameId, AlternateNameDTO alternateNameDTO) {
        try {
            addAuthorization(
                    restClient.post()
                            .uri(apiUrl + "/" + boardGameId + "/alternatenames")
                            .body(alternateNameDTO)
            ).retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
    }

    public void updateAlternateName(Long boardGameId, Long id, AlternateNameDTO alternateNameDTO) {
        try {
            addAuthorization(
                    restClient.put()
                            .uri(apiUrl + "/" + boardGameId + "/alternatenames/" + id)
                            .body(alternateNameDTO)
            ).retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
    }

    public void deleteAlternateName(Long boardGameId, Long id) {
        try {
            addAuthorization(
                    restClient.delete()
                            .uri(apiUrl + "/" + boardGameId + "/alternatenames/" + id)
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