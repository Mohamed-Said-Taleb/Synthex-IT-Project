package com.devsling.fr.exceptions;

import com.devsling.fr.tools.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;

import static com.devsling.fr.tools.Constants.AUTH_SERVICE_NAME;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BackendException.class)
    public ResponseEntity<ErrorException> backendExceptionHandler(BackendException e) {
        ErrorException errorException=ErrorException.builder()
                .code(e.getHttpStatus().value())
                .message(e.getMessage())
                .service(e.getService())
                .status(e.getHttpStatus())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(errorException);
    }
    @ExceptionHandler(WebClientResponseException.BadRequest.class)
    public ResponseEntity<?> badRequestExceptionHandler(WebClientResponseException.BadRequest e) {
        ErrorException errorException = ErrorException.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .service(AUTH_SERVICE_NAME)
                .message(e.getMessage())
                .status(HttpStatus.UNAUTHORIZED)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorException);
    }

}
