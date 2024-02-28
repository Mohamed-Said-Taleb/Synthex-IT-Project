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
public class CandidateProfileResponse {
    @JsonProperty("candidate_info")
    private CandidateDto candidateDto;
    @JsonProperty("profile_image")
    private byte[] profileImage;
}
