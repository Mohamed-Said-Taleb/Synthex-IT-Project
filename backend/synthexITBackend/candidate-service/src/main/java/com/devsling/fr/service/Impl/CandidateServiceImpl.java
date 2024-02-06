package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.repository.CandidateRepository;
import com.devsling.fr.service.CandidateService;
import com.devsling.fr.tools.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    @Override
    public Flux<CandidateDto> getCandidates(){
        return candidateRepository.findAll().map(AppUtils::entityToDto);
    }
    @Override
    public Mono<CandidateDto> getCandidate(Long id){
        return candidateRepository.findById(id).map(AppUtils::entityToDto);
    }

    @Override
    public Mono<CandidateDto> saveCandidate(Mono<CandidateDto> candidateDtoMono) {
        return candidateDtoMono
                .map(AppUtils::dtoToEntity)
                .flatMap(candidateRepository::save)
                .map(AppUtils::entityToDto);
    }
    @Override
    public Mono<CandidateDto> updateCandidate(Mono<CandidateDto> candidateDtoMono,Long id){
        return candidateRepository.findById(id)
                .flatMap(p->candidateDtoMono.map(AppUtils::dtoToEntity)
                        .doOnNext(e->e.setId(id)))
                .flatMap(candidateRepository::save)
                .map(AppUtils::entityToDto);

    }
    @Override
    public Mono<Void> deleteCandidate(Long id){
        return candidateRepository.deleteById(id);
    }

    @Override
    public Mono<CandidateDto> getCandidateByEmail(String email) {
        return candidateRepository.findByEmail(email).map(AppUtils::entityToDto);
    }

}
