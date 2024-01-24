package com.devsling.fr.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthErrorMessage {
    private static final long serialVersionUID = 2401572041950111807L;

    @JsonProperty("error_description")
    private String description;
    @JsonProperty("error")
    private String error;
}
