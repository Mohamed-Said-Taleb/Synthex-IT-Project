package com.devsling.fr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ErrorException> backendExceptionHandler(ErrorException exception) {
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }
}
