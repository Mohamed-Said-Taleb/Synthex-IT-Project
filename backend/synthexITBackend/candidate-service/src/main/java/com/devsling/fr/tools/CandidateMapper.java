package com.devsling.fr.tools;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.model.Candidate;
import org.springframework.beans.BeanUtils;

public class CandidateMapper {
    public static CandidateDto entityToDto(Candidate candidate) {
        CandidateDto candidateDto = new CandidateDto();
        BeanUtils.copyProperties(candidate, candidateDto);
        return candidateDto;
    }

    public static Candidate dtoToEntity(CandidateDto candidateDto) {
        Candidate candidate = new Candidate();
        BeanUtils.copyProperties(candidateDto, candidate);
        return candidate;
    }
}
