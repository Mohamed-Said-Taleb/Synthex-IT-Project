package com.devsling.fr.exceptions;

import com.devsling.fr.tools.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class CandidateException extends AbstractException {


    public CandidateException(String message) {
        super(message);
    }
}
