package com.devsling.fr.controller;

import com.devsling.fr.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Candidate;
import org.openapitools.model.CandidateFullData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CandidateController implements CandidateApi {

    private final CandidateService candidateService;


    @Override
    public Mono<ResponseEntity<CandidateFullData>> createCandidate(Mono<Candidate> candidate, ServerWebExchange exchange) {
        return candidateService.create(candidate)
                .map(createdCandidate -> ResponseEntity.status(HttpStatus.OK).body(createdCandidate));
    }
}
