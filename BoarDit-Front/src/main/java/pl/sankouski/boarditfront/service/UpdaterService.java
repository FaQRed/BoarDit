package pl.sankouski.boarditfront.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;

import java.net.URI;

@Service
public class UpdaterService {

    @Autowired
    private final RestClient restClient;
    private final String apiUrl = "http://localhost:8080/admin/updater/start";
    private String token;

    public UpdaterService(RestClient restClient) {
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

    public String updateBoardGame(int number) {
        try {
            URI uri = URI.create(apiUrl + "?number=" + number);
            return addAuthorization(
                    restClient.get().uri(uri))
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return "Error updating board game.";
    }

    private void handleClientError(HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new RuntimeException("Invalid request: " + e.getResponseBodyAsString());
        } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new RuntimeException("Game not found: " + e.getResponseBodyAsString());
        } else {
            throw new RuntimeException("Client error: " + e.getResponseBodyAsString());
        }
    }

    private void handleServerError(HttpServerErrorException e) {
        throw new RuntimeException("Server error: " + e.getResponseBodyAsString());
    }
}