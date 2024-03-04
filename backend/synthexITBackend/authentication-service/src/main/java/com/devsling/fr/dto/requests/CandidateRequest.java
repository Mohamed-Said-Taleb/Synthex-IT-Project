package com.devsling.fr.dto.requests;

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
public class CandidateRequest {
    @JsonProperty("id")
    Long id;
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
}
