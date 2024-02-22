package com.devsling.fr.service.out;

import com.devsling.fr.dto.Requests.CandidateRequest;
import com.devsling.fr.dto.Responses.CandidateProfileResponse;
import reactor.core.publisher.Mono;

public interface CandidateApiClient {

    Mono<CandidateProfileResponse> saveCandidate(CandidateRequest candidateRequest);
    Mono<CandidateProfileResponse> profileCandidate(String email);

}
