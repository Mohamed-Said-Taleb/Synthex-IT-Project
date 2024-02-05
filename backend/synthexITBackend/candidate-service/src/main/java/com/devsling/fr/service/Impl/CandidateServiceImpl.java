package com.devsling.fr.service.Impl;

import com.devsling.fr.repository.CandidateRepository;
import com.devsling.fr.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Candidate;
import org.openapitools.model.CandidateFullData;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    @Override
    public Mono<CandidateFullData> create(Mono<Candidate> candidate) {

        return candidate.flatMap(candidateRepository::save)
                .mapNotNull(savedCandidate -> {
                    return null;
                });
    }

}
