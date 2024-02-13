package com.devsling.fr.dto.Responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private int code;

    @JsonIgnore
    private String message;

    @JsonIgnore
    private String service;

    @JsonIgnore
    private String dataLabel;

    @JsonIgnore
    private String subCode;
}
