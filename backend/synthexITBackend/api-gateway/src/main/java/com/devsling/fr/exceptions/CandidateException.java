package com.devsling.fr.exceptions;

import com.devsling.fr.tools.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateException extends BackendException{
    public CandidateException(String service, String message, String mainResource,
                                int httpStatus) {
        super(Constants.CANDIDATE_SERVICE_NAME,
                message,
                mainResource,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
