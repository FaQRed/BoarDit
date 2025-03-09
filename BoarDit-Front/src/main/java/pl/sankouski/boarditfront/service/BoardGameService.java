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
import pl.sankouski.boarditfront.dto.BoardGameDTO;
import pl.sankouski.boarditfront.dto.BoardGameSummaryDto;
import pl.sankouski.boarditfront.dto.BoardGameUpdateSummaryDTO;
import pl.sankouski.boarditfront.mapper.BoardGameMapper;

import java.util.List;

@Service
public class BoardGameService {
    @Autowired
    private final RestClient restClient;
    private final String apiUrl = "http://localhost:8080/boardgames";
    private String token;

    public BoardGameService(RestClient restClient) {
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

    public List<BoardGameDTO> getAllBoardGames() {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl)
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<BoardGameDTO>>() {});
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public BoardGameDTO getBoardGameById(Long id) {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/" + id)
            ).retrieve()
                    .body(BoardGameDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public BoardGameDTO createBoardGame(BoardGameDTO boardGameDTO) {
        try {
            return addAuthorization(
                    restClient.post()
                            .uri(apiUrl)
                            .body(boardGameDTO)
            ).retrieve()
                    .body(BoardGameDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public BoardGameUpdateSummaryDTO updateBoardGame(Long id, BoardGameUpdateSummaryDTO boardGameDTO) {
        try {
            return addAuthorization(
                    restClient.put()
                            .uri(apiUrl + "/" + id)
                            .body(boardGameDTO)
            ).retrieve()
                    .body(BoardGameUpdateSummaryDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public void deleteBoardGame(Long id) {
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

    public List<BoardGameSummaryDto> getAllBoardGamesSummary() {
        try {
            List<BoardGameDTO> boardGames = addAuthorization(restClient.get()
                    .uri(apiUrl))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<BoardGameDTO>>() {});

            return BoardGameMapper.toSummaryList(boardGames);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch board games: " + e.getMessage());
        }
    }


    public void addCategoryToBoardGame(Long boardGameId, Long categoryId) {
        addAuthorization(
                restClient.post()
                        .uri(apiUrl + "/" + boardGameId + "/categories/" + categoryId)
        ).retrieve().toBodilessEntity();
    }

    public void removeCategoryFromBoardGame(Long boardGameId, Long categoryId) {
        addAuthorization(
                restClient.delete()
                        .uri(apiUrl + "/" + boardGameId + "/categories/" + categoryId)
        ).retrieve().toBodilessEntity();
    }

    public void addMechanicToBoardGame(Long boardGameId, Long mechanicId) {
        addAuthorization(
                restClient.post()
                        .uri(apiUrl + "/" + boardGameId + "/mechanics/" + mechanicId)
        ).retrieve().toBodilessEntity();
    }

    public void removeMechanicFromBoardGame(Long boardGameId, Long mechanicId) {
        addAuthorization(
                restClient.delete()
                        .uri(apiUrl + "/" + boardGameId + "/mechanics/" + mechanicId)
        ).retrieve().toBodilessEntity();
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