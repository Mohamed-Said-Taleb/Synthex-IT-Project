package com.devsling.fr.service.out;

import com.devsling.fr.dto.requests.EmployerCreateRequest;
import reactor.core.publisher.Mono;

public interface EmployerApiClient {
    Mono<EmployerCreateRequest> saveEmployer(EmployerCreateRequest employerCreateRequest);
}
