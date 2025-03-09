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
import pl.sankouski.boarditfront.dto.CategoryDTO;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private final RestClient restClient;
    private final String apiUrl = "http://localhost:8080/categories";
    private String token;

    public CategoryService(RestClient restClient) {
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

    public List<CategoryDTO> getAllCategories() {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl)
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<CategoryDTO>>() {});
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        try {
            return addAuthorization(
                    restClient.post()
                            .uri(apiUrl)
                            .body(categoryDTO)
            ).retrieve()
                    .body(CategoryDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public void deleteCategory(Long id) {
        try {
            addAuthorization(
                    restClient.delete()
                            .uri(apiUrl + "/" + id)
            ).retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
    }

    public void updateCategory(CategoryDTO categoryDTO, Long id) {
        try {
            addAuthorization(
                    restClient.put()
                            .uri(apiUrl + "/" + id)
                            .body(categoryDTO)
            ).retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
    }

    public List<CategoryDTO> getCategoriesByIds(List<Long> ids) {
        try {
            String idsParam = String.join(",", ids.stream().map(String::valueOf).toList());
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/list/" + idsParam)
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<CategoryDTO>>() {});
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