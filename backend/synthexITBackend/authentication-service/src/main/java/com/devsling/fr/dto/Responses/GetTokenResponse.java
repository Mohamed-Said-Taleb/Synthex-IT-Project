package com.devsling.fr.dto.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GetTokenResponse {
    @JsonProperty("token")
    private String token;
    @JsonProperty("message")
    private String message;
}