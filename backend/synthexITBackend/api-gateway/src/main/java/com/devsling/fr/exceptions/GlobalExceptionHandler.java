package com.devsling.fr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public Mono<ResponseStatusException> handleAuthenticationException(AuthenticationException ex) {
        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex));
    }

    @ExceptionHandler(WebClientRequestException.class)
    public Mono<ResponseStatusException> handleWebClientRequestException(WebClientRequestException ex) {
        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error calling auth service", ex));
    }

    @ExceptionHandler(MissingAuthorizationHeaderException.class)
    public Mono<ResponseStatusException> handleException(Exception ex) {
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Missing authorization header", ex));
    }
}
