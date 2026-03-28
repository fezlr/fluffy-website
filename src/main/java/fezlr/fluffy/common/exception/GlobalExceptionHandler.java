package fezlr.fluffy.common.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handlerGeneralException(Exception e) {
        log.error("Called handlerGeneralException", e);
        var error = new ErrorResponseDto(
                "Internal server error",
                "Unexpected error occurred",
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handlerDataIntegrityException(Exception e) {
        log.error("Called handlerDataIntegrityException", e);
        var error = new ErrorResponseDto(
                "Conflict",
                "Valuable already exists",
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlerEntityNotFoundException(Exception e) {
        log.error("Called handlerEntityNotFoundException", e);
        var error = new ErrorResponseDto(
                "Not found",
                "Entity is not found",
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(exception = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponseDto> handlerBadRequest(Exception e) {
        log.error("Handler exception", e);
        var errorDto = new ErrorResponseDto(
                "Bad request",
                "Validation failed",
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }
}
