package com.devsling.fr.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractException extends RuntimeException{
    @JsonIgnore
    private int httpStatus; //NOSONAR
    @JsonIgnore
    private String mainResource; //NOSONAR

    protected AbstractException(int httpStatus, String mainResource) {
        this.httpStatus = httpStatus;
        this.mainResource = mainResource;
    }
}
