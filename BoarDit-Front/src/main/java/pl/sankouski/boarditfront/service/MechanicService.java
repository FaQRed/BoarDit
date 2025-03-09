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
import pl.sankouski.boarditfront.dto.MechanicDTO;


import java.util.List;

@Service
public class MechanicService {

    @Autowired
    private final RestClient restClient;
    private final String apiUrl = "http://localhost:8080/mechanics";
    private String token;

    public MechanicService(RestClient restClient) {
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

    public List<MechanicDTO> getAllMechanics() {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl)
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<MechanicDTO>>() {});
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public MechanicDTO getMechanicById(Long id) {
        try {
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/" + id)
            ).retrieve()
                    .body(MechanicDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public MechanicDTO createMechanic(MechanicDTO mechanicDTO) {
        try {
            return addAuthorization(
                    restClient.post()
                            .uri(apiUrl)
                            .body(mechanicDTO)
            ).retrieve()
                    .body(MechanicDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public MechanicDTO updateMechanic(Long id, MechanicDTO mechanicDTO) {
        try {
            return addAuthorization(
                    restClient.put()
                            .uri(apiUrl + "/" + id)
                            .body(mechanicDTO)
            ).retrieve()
                    .body(MechanicDTO.class);
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        }
        return null;
    }

    public void deleteMechanic(Long id) {
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

    public List<MechanicDTO> getMechanicsByIds(List<Long> ids) {
        try {
            String idsParam = String.join(",", ids.stream().map(String::valueOf).toList());
            return addAuthorization(
                    restClient.get().uri(apiUrl + "/list/" + idsParam)
            ).retrieve()
                    .body(new ParameterizedTypeReference<List<MechanicDTO>>() {});
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