package com.devsling.fr.service;

import com.devsling.fr.dto.CandidateDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CandidateService {
    Flux<CandidateDto> getCandidates();
    Mono<CandidateDto> getCandidateById(Long id);
    Mono<CandidateDto> saveCandidate(Mono<CandidateDto> candidateDtoMono);
    Mono<CandidateDto> updateCandidate(Mono<CandidateDto> candidateDtoMono, Long id);
    Mono<Void> deleteCandidateById(Long id);
    Mono<CandidateDto> getCandidateByEmail(String email);
}
