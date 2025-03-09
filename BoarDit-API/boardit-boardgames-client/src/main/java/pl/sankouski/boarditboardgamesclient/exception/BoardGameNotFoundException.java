package pl.sankouski.boarditboardgamesclient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BoardGameNotFoundException extends RuntimeException{
    public BoardGameNotFoundException(Long id) {
        super("Board game with ID " + id + " not found on BoardGameGeek");
    }
}
