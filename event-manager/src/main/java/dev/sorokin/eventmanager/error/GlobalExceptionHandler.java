package dev.sorokin.eventmanager.error;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handleEntityNotFoundException(EntityNotFoundException e) {
        logger.error("Got entity not found exception", e);
        var errorDto = new ServerErrorDto(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(404).body(errorDto);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerErrorDto> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("Got argument not valid exception", e);

        String detailedMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        var errorDto = new ServerErrorDto(
                "Некорректный запрос",
                detailedMessage,
                LocalDateTime.now()
        );
        return ResponseEntity.status(400).body(errorDto);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServerErrorDto> handleIllegalArgumentNotValidException(IllegalArgumentException e) {
        logger.error("Got illegal argument not valid exception", e);

        var errorDto = new ServerErrorDto(
                "Некорректный запрос",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(400).body(errorDto);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerErrorDto> handleGenerisException(Exception e) {
        logger.error("Got generis exception", e);
        var errorDto = new ServerErrorDto(
                "Внутренняя ошибка сервера",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(500).body(errorDto);

    }


}
