package pl.sankouski.boarditfront.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleClientError_BadRequest() {
        HttpClientErrorException exception = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST, "Bad Request", null, "Invalid data".getBytes(), null);
        Model model = mock(Model.class);


        String viewName = exceptionHandler.handleClientError(exception, model);

        assertEquals("error", viewName);
        verify(model).addAttribute("error", "Invalid data provided: Invalid data");
    }

    @Test
    void testHandleClientError_NotFound() {

        HttpClientErrorException exception = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND, "Not Found", null, "Resource missing".getBytes(), null);
        Model model = mock(Model.class);

        String viewName = exceptionHandler.handleClientError(exception, model);
        assertEquals("error", viewName);
        verify(model).addAttribute("error", "Resource not found: Resource missing");
    }

    @Test
    void testHandleClientError_ConflictWithJsonMessage() throws JsonProcessingException {
        String conflictResponseJson = "{\"message\":\"Conflict occurred\"}";
        HttpClientErrorException exception = HttpClientErrorException.create(
                HttpStatus.CONFLICT, "Conflict", null, conflictResponseJson.getBytes(), null);
        Model model = mock(Model.class);
        String viewName = exceptionHandler.handleClientError(exception, model);

        assertEquals("error", viewName);
        verify(model).addAttribute("error", "Conflict error: Conflict occurred");
    }

    @Test
    void testHandleServerError() {
        HttpServerErrorException exception = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null, "Critical error".getBytes(), null);
        Model model = mock(Model.class);

        String viewName = exceptionHandler.handleServerError(exception, model);
        assertEquals("error", viewName);
        verify(model).addAttribute("error", "Server error occurred: Critical error");
    }

    @Test
    void testHandleGenericError() {
        Exception exception = new Exception("Something went wrong");
        Model model = mock(Model.class);

        String viewName = exceptionHandler.handleGenericError(exception, model);

        assertEquals("error", viewName);
        verify(model).addAttribute("error", "Unexpected error occurred: Something went wrong");
    }
}