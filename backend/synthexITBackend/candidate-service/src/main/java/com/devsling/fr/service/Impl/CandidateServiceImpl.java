package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.dto.CandidateProfileResponse;
import com.devsling.fr.dto.UploadImageResponse;
import com.devsling.fr.exceptions.CandidateException;
import com.devsling.fr.repository.CandidateRepository;
import com.devsling.fr.service.CandidateService;
import com.devsling.fr.service.FileStorageService;
import com.devsling.fr.tools.CandidateMapper;
import com.devsling.fr.tools.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final FileStorageService fileStorageService;

    @Override
    public Flux<CandidateProfileResponse> getCandidates() {
        return candidateRepository.findAll()
                .flatMap(candidate ->
                        getProfileImage(candidate.getImageName())
                                .map(imageData -> new CandidateProfileResponse(CandidateMapper
                                        .entityToDto(candidate), imageData))
                                .switchIfEmpty(Mono.error(new CandidateException(Constants.NO_CANDIDATE_FOUND)))
                );
    }

    @Override
    public Mono<CandidateProfileResponse> getCandidateById(Long id){
        Mono<CandidateDto> candidateDtoMono = candidateRepository.findById(id)
                .map(CandidateMapper::entityToDto)
                .switchIfEmpty(Mono.error(new CandidateException(Constants.NO_CANDIDATE_WITH_EMAIL_FOUND)));

        return candidateDtoMono.flatMap(candidateDto ->
                getProfileImage(candidateDto.getImageName())
                        .map(imageData -> new CandidateProfileResponse(candidateDto, imageData))
        );
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
    public Mono<CandidateProfileResponse> getCandidateProfile(String email) {
        Mono<CandidateDto> candidateDtoMono = candidateRepository.findByEmail(email)
                .map(CandidateMapper::entityToDto)
                .switchIfEmpty(Mono.error(new CandidateException(Constants.NO_CANDIDATE_WITH_EMAIL_FOUND)));

        return candidateDtoMono.flatMap(candidateDto ->
                getProfileImage(candidateDto.getImageName())
                        .map(imageData -> new CandidateProfileResponse(candidateDto, imageData))
        );
    }

    @Override
    public Mono<UploadImageResponse> uploadCandidateImage(MultipartFile file, Long candidateId) throws IOException {
        return fileStorageService.uploadImage(file)
                .flatMap(imageUrl -> candidateRepository.findById(candidateId)
                        .flatMap(candidate -> {
                            candidate.setImageName(file.getOriginalFilename());
                            return candidateRepository.save(candidate)
                                    .map(savedCandidate -> UploadImageResponse.builder()
                                            .message(imageUrl.getMessage()).build());
                        })
                        .switchIfEmpty(Mono.error(new CandidateException(Constants.NO_CANDIDATE_FOUND_WITH_ID_+candidateId))))
                .onErrorResume(CandidateException.class, ex -> Mono.just(new UploadImageResponse(ex.getMessage())));
    }

    @Override
    public Mono<byte[]> getProfileImage(String fileName) {
        return fileStorageService.downloadImage(fileName);
    }
}
