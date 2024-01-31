package com.devsling.fr.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = 15564L;

    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("correlation-id")
    private String correlationId;

    @JsonProperty("timestamp")
    private String timeStamp;

}