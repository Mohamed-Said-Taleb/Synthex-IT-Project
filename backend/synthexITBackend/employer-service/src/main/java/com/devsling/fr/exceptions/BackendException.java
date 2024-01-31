package com.devsling.fr.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class BackendException extends RuntimeException {

    @JsonIgnore
    private String mainResource; //NOSONAR
    @JsonIgnore
    private int httpStatus; //NOSONAR
    @JsonIgnore
    private String subCode; //NOSONAR
    @JsonIgnore
    private String message; //NOSONAR
    @JsonIgnore
    private String service; //NOSONAR

    public void log(Logger logger) {
        logger.error("{} | {} | {}| {}", getMainResource(), getHttpStatus(), getMessage(), getService());
    }
}
