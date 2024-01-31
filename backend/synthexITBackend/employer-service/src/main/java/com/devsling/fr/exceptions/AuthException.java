package com.devsling.fr.exceptions;

import com.devsling.fr.tools.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthException extends BackendException{
    public AuthException(int httpStatus, String mainResource, ErrorMessage errorMessage) {

        super(mainResource,
                httpStatus,
                null,
                (errorMessage != null && errorMessage.getMessage() != null) ? errorMessage.getMessage() : "HTTP - " + httpStatus,
                Constants.AUTH_SERVICE);

    }
}
