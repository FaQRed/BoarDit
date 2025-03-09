package pl.sankouski.boarditboardgamesclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import pl.sankouski.boarditboardgamesclient.exception.BoardGameNotFoundException;
import pl.sankouski.boarditboardgamesclient.exception.GameAlreadyExistsException;
import pl.sankouski.boarditboardgamesclient.exception.UserAlreadyExistsException;
import pl.sankouski.boarditboardgamesclient.exception.UserNotFoundException;
import pl.sankouski.boarditboardgamesclient.exceptionHandler.GlobalExceptionHandler;
import pl.sankouski.boarditboardgamesclient.exceptionHandler.GlobalExceptionHandler.ErrorResponse;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    public void setup() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void handleGameAlreadyExistsException_shouldReturnConflictStatus() {
        GameAlreadyExistsException exception = new GameAlreadyExistsException("Game already exists");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGameAlreadyExistsException(exception);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Game already exists", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void handleUserAlreadyExistsException_shouldReturnConflictStatus() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("User already exists");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUserAlreadyExistsException(exception);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("User already exists", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void handleIllegalArgumentException_shouldReturnConflictStatus() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Invalid argument", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void handleExpiredJwtException_shouldReturnUnauthorizedStatus() {
        io.jsonwebtoken.ExpiredJwtException exception = Mockito.mock(io.jsonwebtoken.ExpiredJwtException.class);
        Mockito.when(exception.getMessage()).thenReturn("JWT token has expired");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleExpiredJwtException(exception);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("JWT token has expired", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void handleGlobalException_shouldReturnInternalServerError() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGlobalException(exception);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Internal server error: Unexpected error", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void handleUsernameNotFoundException_shouldReturnConflictStatus() {
        org.springframework.security.core.userdetails.UsernameNotFoundException exception =
                new org.springframework.security.core.userdetails.UsernameNotFoundException("Username not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUsernameNotFoundException(exception);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Username not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void handleBoardGameNotFoundException_shouldReturnNotFoundStatus() {
        Long boardGameId = 42L;
        BoardGameNotFoundException exception = new BoardGameNotFoundException(boardGameId);
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBoardGameNotFoundException(exception);
        assertEquals(404, response.getStatusCode().value());

        assertEquals("Board game with ID 42 not found on BoardGameGeek", exception.getMessage());
    }

    @Test
    public void handleUserNotFoundException_shouldReturnNotFoundStatus() {
        UserNotFoundException exception = new UserNotFoundException("User not found");


        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUserNotFoundException(exception);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("User not found", Objects.requireNonNull(response.getBody()).getMessage());
    }
}