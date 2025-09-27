package dev.pet.mvc.exceptions;

import dev.pet.mvc.model.ServerErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerErrorDto> handleValidationExceptions(MethodArgumentNotValidException e) {
        LOGGER.error("Valid exception: ", e);

        ServerErrorDto serverErrorDto = new ServerErrorDto("Got server error", e.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(serverErrorDto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ServerErrorDto> handleNotFoundException(NoSuchElementException e) {
        LOGGER.error("No such element exception: ", e);

        ServerErrorDto errorDto =  new ServerErrorDto("Entity has not found", e.getMessage(), LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ServerErrorDto> handleGeneralException(Exception e) {
        LOGGER.error("General exception: ", e);

        ServerErrorDto errorDto =  new ServerErrorDto("General error while handle response",  e.getMessage(),  LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }
}
