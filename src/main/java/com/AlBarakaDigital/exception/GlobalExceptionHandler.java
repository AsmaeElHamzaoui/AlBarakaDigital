package com.AlBarakaDigital.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildErrorResponse(
            String message,
            HttpStatus status,
            HttpServletRequest request
    ) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, status);
    }

    // Lorsqu'un email existe déjà
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Object> handleUsernameExists(
            UsernameAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    // Lorsqu’un compte n’existe pas
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleEventNotFound(
            AccountNotFoundException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    // Action non autorisée - 403
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Object> handleUnauthorizedAction(
            UnauthorizedActionException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    // Fallback pour toute autre exception
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntime(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }


}
