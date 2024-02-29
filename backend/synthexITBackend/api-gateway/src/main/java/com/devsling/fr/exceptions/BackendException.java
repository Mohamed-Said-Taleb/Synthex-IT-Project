package com.devsling.fr.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class BackendException extends RuntimeException{
    @JsonIgnore
    private String service;//NOSONAR
    @JsonIgnore
    private String message; //NOSONAR
    @JsonIgnore
    private String mainResource; //NOSONAR
    @JsonIgnore
    private HttpStatus httpStatus; //NOSONAR



    public void log(Logger logger) {
        logger.error("{}", getMessage());
    }
}
