package com.devsling.fr.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class FIleUploadException extends AbstractException{
    public FIleUploadException(String message) {
        super(message);
    }
}
