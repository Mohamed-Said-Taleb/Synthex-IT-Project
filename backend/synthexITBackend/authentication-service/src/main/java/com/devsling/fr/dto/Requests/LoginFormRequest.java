package com.devsling.fr.dto.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginFormRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
