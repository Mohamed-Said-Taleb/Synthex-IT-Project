package com.devsling.fr.dto;

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

    @JsonProperty("type")
    private String type = "Bearer";

    @JsonProperty("username")
    private String username;
}
