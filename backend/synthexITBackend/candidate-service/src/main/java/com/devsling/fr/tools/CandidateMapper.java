package com.devsling.fr.tools;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.dto.CandidateProfileResponse;
import com.devsling.fr.model.Candidate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper {
    public static CandidateDto entityToDto(Candidate candidate) {
        CandidateDto candidateDto = new CandidateDto();
        BeanUtils.copyProperties(candidate, candidateDto);
        return candidateDto;
    }
    public static CandidateProfileResponse candidateToCandidateProfile(Candidate candidate) {
        CandidateProfileResponse candidateProfileResponse = new CandidateProfileResponse();
        BeanUtils.copyProperties(candidate, candidateProfileResponse);
        return candidateProfileResponse;
    }

    public static Candidate dtoToEntity(CandidateDto candidateDto) {
        Candidate candidate = new Candidate();
        BeanUtils.copyProperties(candidateDto, candidate);
        return candidate;
    }

}
