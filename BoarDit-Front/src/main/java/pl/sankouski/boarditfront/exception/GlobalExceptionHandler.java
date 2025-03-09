package pl.sankouski.boarditfront.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleClientError(HttpClientErrorException e, Model model) {
        String errorMessage = "Client error occurred.";
        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
            errorMessage = "Invalid data provided: " + e.getResponseBodyAsString();
        } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            errorMessage = "Resource not found: " + e.getResponseBodyAsString();
        } else if (e.getStatusCode() == HttpStatus.CONFLICT) {
            errorMessage = "Conflict error: " + extractMessageFromJson(e.getResponseBodyAsString());
        }

        model.addAttribute("error", errorMessage);
        return "error";
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public String handleServerError(HttpServerErrorException e, Model model) {
        String errorMessage = "Server error occurred: " + e.getResponseBodyAsString();
        model.addAttribute("error", errorMessage);
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericError(Exception e, Model model) {
        String errorMessage = "Unexpected error occurred: " + e.getMessage();
        model.addAttribute("error", errorMessage);
        return "error";
    }

    private String extractMessageFromJson(String json) {
        try {
            return new ObjectMapper().readTree(json).get("message").asText();
        } catch (JsonProcessingException e) {
            return "Error parsing error message.";
        }
    }
}