package com.devsling.fr.service.out;

import com.devsling.fr.dto.Requests.CandidateRequest;
import com.devsling.fr.dto.Responses.CandidateCreateResponse;
import reactor.core.publisher.Mono;

public interface CandidateApiClient {

    Mono<CandidateCreateResponse> saveCandidate(CandidateRequest candidateRequest);

}
