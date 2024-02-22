package com.devsling.fr.service.out;

import com.devsling.fr.dto.Requests.EmployerCreateRequest;
import com.devsling.fr.dto.Responses.CandidateProfileResponse;
import reactor.core.publisher.Mono;

public interface EmployerApiClient {
    Mono<EmployerCreateRequest> saveEmployer(EmployerCreateRequest employerCreateRequest);
}
