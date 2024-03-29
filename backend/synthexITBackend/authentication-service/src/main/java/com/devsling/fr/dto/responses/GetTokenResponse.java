package com.devsling.fr.dto.responses;

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

    @JsonProperty("access-token")
    private String token;
    @JsonProperty("refresh-token")
    private String refreshToken;
    @JsonProperty("message")
    private String message;
}
