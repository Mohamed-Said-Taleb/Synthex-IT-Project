package com.devsling.fr.exceptions;

import com.devsling.fr.tools.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<?> backendExceptionHandler(AbstractException e) {
        ErrorException errorException=ErrorException.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .service(Constants.CANDIDATE_SERVICE_NAME)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorException);
    }
}
