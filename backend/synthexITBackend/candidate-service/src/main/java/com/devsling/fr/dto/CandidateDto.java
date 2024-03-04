package com.devsling.fr.dto;

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
public class CandidateDto {

    @JsonProperty("id")
    private Long id;

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

    @JsonProperty("currentPosition")
    private CurrentSituation currentPosition;

    @JsonProperty("jobAvailability")
    private JobAvailability jobAvailability;

    @JsonProperty("experienceLevel")
    private ExperienceLevel experienceLevel;

    @JsonProperty("qualificationLevel")
    private String qualificationLevel;

    @JsonProperty("industrySector")
    private List<String> industrySector;

    @JsonProperty("salary")
    private SalaryRange salary;

    @JsonProperty("disabledWorker")
    private boolean disabledWorker;

    @JsonProperty("imageName")
    private String imageName;
}
