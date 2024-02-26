package com.devsling.fr.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class BackendException extends RuntimeException{

    private String message; //NOSONAR

    public void log(Logger logger) {
        logger.error("{}", getMessage());
    }
}
