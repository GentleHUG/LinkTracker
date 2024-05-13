package edu.java.bot.controller;

import edu.java.bot.controller.dto.ErrorResponse;
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
    public ResponseEntity<?> handler(Exception ex) {
        List<String> stacktrace = Arrays.stream(ex.getStackTrace()).map(Object::toString).toList();
        ErrorResponse errorResponse = new ErrorResponse(
            "400",
            "Неверный формат url",
            ex.getClass().getSimpleName(),
            ex.getMessage(),
            stacktrace
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
