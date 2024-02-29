package com.devsling.fr.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatewayTimeoutException extends BackendException{

    public GatewayTimeoutException(String service, String message, String mainResource,
                                HttpStatus httpStatus) {
        super(service,
                message,
                mainResource,
                httpStatus);
    }
}
