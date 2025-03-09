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
import pl.sankouski.boarditfront.dto.ArtistDTO;

import java.util.List;

@Service
public class ArtistService {
    @Autowired
    private final RestClient restClient;

    private final String apiUrl = "http://localhost:8080/artists";
    private String token;

    public ArtistService(RestClient restClient) {
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

    public List<ArtistDTO> getAllArtists() {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl)
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<ArtistDTO>>() {});
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public ArtistDTO getArtistById(Long id) {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/" + id)
            ).retrieve()
                    .body(ArtistDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public ArtistDTO createArtist(ArtistDTO artistDTO) {
        try {
            return addAuthorization(
                    restClient.post()
                            .uri(apiUrl)
                            .body(artistDTO)
            ).retrieve()
                    .body(ArtistDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public void createArtistForBoardGame(Long boardGameId, ArtistDTO artistDTO) {
        try {
            addAuthorization(
                    restClient.post()
                            .uri(apiUrl + "/" + boardGameId)
                            .body(artistDTO)
            ).retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
    }

    public ArtistDTO updateArtist(Long id, ArtistDTO artistDTO) {
        try {
            return addAuthorization(
                    restClient.put()
                            .uri(apiUrl + "/" + id)
                            .body(artistDTO)
            ).retrieve()
                    .body(ArtistDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public void deleteArtist(Long id) {
        try {
            addAuthorization(
                    restClient.delete().uri(apiUrl + "/" + id)
            ).retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
    }
    public List<ArtistDTO> getArtistsByNames(List<String> names) {
        try {
            String namesParam = String.join(",", names);
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/names?names=" + namesParam)
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<ArtistDTO>>() {});
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
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