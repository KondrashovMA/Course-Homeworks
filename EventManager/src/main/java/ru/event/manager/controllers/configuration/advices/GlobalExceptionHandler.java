package ru.event.manager.controllers.configuration.advices;

import jakarta.persistence.EntityNotFoundException;
import jakarta.xml.bind.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.event.manager.controllers.configuration.model.ServerErrorDto;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    public ResponseEntity<ServerErrorDto> handleValidationException(Exception e) {
        log.error("Got validation exception", e);

        ServerErrorDto serverErrorDto = new ServerErrorDto(
                "Ошибка во время валидации запроса",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(serverErrorDto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ServerErrorDto> handleNoSuchElementException(NoSuchElementException e) {
        log.error("Got no such element exception", e);

        ServerErrorDto serverErrorDto = new ServerErrorDto(
                "Не найдена сущность:",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(serverErrorDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handleEntityNotFoundException(
            EntityNotFoundException e
    ) {
        log.error("Got exception", e);
        var errorDto =  new ServerErrorDto(
                "Сущность базы данных не найдена",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ServerErrorDto> handleGenerisException(
            Exception e
    ) {
        log.error("Server error", e);
        var errorDto =  new ServerErrorDto(
                "Возникла ошибка во время работы",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ServerErrorDto> handleBadCredentialsException(
            BadCredentialsException e
    ) {
        log.error("Server error", e);
        var errorDto =  new ServerErrorDto(
                "Некорректные логин и пароль для пользователя",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }
}
