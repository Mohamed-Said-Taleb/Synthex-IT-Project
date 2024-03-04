package com.devsling.fr.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CandidateProfileResponse {
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("skills")
    private List<String> skills;
    @JsonProperty("resumeUrl")
    private String resumeUrl;
    @JsonProperty("professionalExperiences")
    private List<String> professionalExperiences;
    @JsonIgnore
    private ErrorMessageResponse errorResponse;
}
