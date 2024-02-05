package com.devsling.fr.service;

import org.openapitools.model.Candidate;
import org.openapitools.model.CandidateFullData;
import reactor.core.publisher.Mono;

public interface CandidateService {
    Mono<CandidateFullData> create(Mono<Candidate> candidate);
}
