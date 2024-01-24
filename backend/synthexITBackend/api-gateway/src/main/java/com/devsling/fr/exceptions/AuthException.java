package com.devsling.fr.exceptions;

import com.devsling.fr.tools.Constant;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthException extends AbstractException {
    private AuthErrorMessage errorResponse;

    public AuthException(int httpStatus, String mainResource, AuthErrorMessage errorResponse) {
        super(httpStatus, mainResource);
        this.errorResponse = errorResponse;
    }

    public AuthErrorMessage getErrorResponse() {
        return errorResponse;
    }
}

