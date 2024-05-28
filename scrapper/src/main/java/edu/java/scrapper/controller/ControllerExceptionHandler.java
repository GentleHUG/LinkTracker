package edu.java.scrapper.controller;

import edu.java.scrapper.controller.dto.ErrorResponse;
import edu.java.scrapper.exception.ExistLinkException;
import edu.java.scrapper.exception.ExistChatException;
import edu.java.scrapper.exception.NotFoundChatException;
import edu.java.scrapper.exception.NotFoundLinkException;
import edu.java.scrapper.exception.RateLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidLinkHandle(Exception ex) {
        ErrorResponse errorResponse = createErrorResponse(ex, "Неверный формат url", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistLinkException.class)
    public ResponseEntity<ErrorResponse> existLinkHandler(Exception ex) {
        ErrorResponse errorResponse = createErrorResponse(ex, "Ссылка уже добавлена", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistChatException.class)
    public ResponseEntity<ErrorResponse> existChatHandler(Exception ex) {
        ErrorResponse errorResponse = createErrorResponse(ex, "Чат уже добавлен", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundLinkException.class)
    public ResponseEntity<ErrorResponse> notFoundLinkHandler(Exception ex) {
        ErrorResponse errorResponse = createErrorResponse(ex, "Ссылка не найдена", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundChatException.class)
    public ResponseEntity<ErrorResponse> notFoundChatHandler(Exception ex) {
        ErrorResponse errorResponse = createErrorResponse(ex, "Чат с таким ID не найден", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> rateLimitExceededHandler(Exception ex) {
        ErrorResponse errorResponse = createErrorResponse(ex, "Слишком много запрососов", HttpStatus.TOO_MANY_REQUESTS);
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    private ErrorResponse createErrorResponse(Exception ex, String message, HttpStatus httpStatus) {
        List<String> stacktrace = Arrays.stream(ex.getStackTrace()).map(Object::toString).toList();

        return new ErrorResponse(
            String.valueOf(httpStatus.value()),
            message,
            ex.getClass().getSimpleName(),
            ex.getMessage(),
            stacktrace
        );
    }
}
