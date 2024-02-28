package com.devsling.fr.service;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.exceptions.CandidateException;
import com.devsling.fr.repository.CandidateRepository;
import com.devsling.fr.tools.CandidateMapper;
import com.devsling.fr.tools.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    @Override
    public Flux<CandidateDto> getCandidates() {
        return candidateRepository.findAll()
                .map(CandidateMapper::entityToDto)
                .switchIfEmpty(Flux.error(new CandidateException(Constants.NO_CANDIDATE_FOUND)));
    }

    @Override
    public Mono<CandidateDto> getCandidateById(Long id){
        return candidateRepository.findById(id).map(CandidateMapper::entityToDto)
                .switchIfEmpty(Mono.error(new CandidateException(Constants
                        .NO_CANDIDATE_FOUND_WITH_ID_+id)));
    }

    @Override
    public Mono<CandidateDto> saveCandidate(Mono<CandidateDto> candidateDtoMono) {
        return candidateDtoMono
                .map(CandidateMapper::dtoToEntity)
                .flatMap(candidateRepository::save)
                .map(CandidateMapper::entityToDto)
                .switchIfEmpty(Mono.error(new CandidateException(Constants
                        .SAVE_CANDIDATE_ERROR_MESSAGE)));
    }
    @Override
    public Mono<CandidateDto> updateCandidate(Mono<CandidateDto> candidateDtoMono,Long id){
        return candidateRepository.findById(id)
                .flatMap(p->candidateDtoMono.map(CandidateMapper::dtoToEntity)
                        .doOnNext(e->e.setId(id)))
                .flatMap(candidateRepository::save)
                .map(CandidateMapper::entityToDto)
                .switchIfEmpty(Mono.error(new CandidateException(Constants.UPDATE_CANDIDATE_ERROR_MESSAGE)));


    }
    @Override
    public Mono<Void> deleteCandidateById(Long id){
        return candidateRepository.findById(id)
                .flatMap(candidate -> {
                    if (candidate != null) {
                        return candidateRepository.deleteById(id);
                    } else {
                        return Mono.error(new CandidateException(Constants.DELETE_CANDIDATE_ERROR_MESSAGE));
                    }
                });
    }

    @Override
    public Mono<CandidateDto> getCandidateByEmail(String email) {
        return candidateRepository.findByEmail(email).map(CandidateMapper::entityToDto)
                .switchIfEmpty(Mono.error(new CandidateException(Constants.NO_CANDIDATE_WITH_EMAIL_FOUND)));

    }
}
