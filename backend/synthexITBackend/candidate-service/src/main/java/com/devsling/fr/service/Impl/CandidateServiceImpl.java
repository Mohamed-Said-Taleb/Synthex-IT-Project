package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.dto.CandidateProfileResponse;
import com.devsling.fr.dto.ImageResponse;
import com.devsling.fr.dto.UploadImageResponse;
import com.devsling.fr.exceptions.CandidateException;
import com.devsling.fr.repository.CandidateRepository;
import com.devsling.fr.repository.ImageStorageRepository;
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

import static com.devsling.fr.tools.Constants.IMAGE_FOUND;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final FileStorageService fileStorageService;
    private final ImageStorageRepository imageStorageRepository;

    @Override
    public Flux<CandidateProfileResponse> getCandidates() {
        return candidateRepository.findAll()
                .flatMap(candidate -> getProfileImage(candidate.getImageName())
                        .map(imageResponse ->
                                createCandidateProfileResponse(imageResponse,
                                CandidateMapper.entityToDto(candidate)))
                        .defaultIfEmpty(CandidateProfileResponse.builder()
                                .candidateDto(CandidateMapper.entityToDto(candidate))
                                .profileImage(null)
                                .build())
                );
    }
    @Override
    public Mono<CandidateProfileResponse> getCandidateById(Long id) {
        Mono<CandidateDto> candidateDtoMono = candidateRepository.findById(id)
                .map(CandidateMapper::entityToDto)
                .switchIfEmpty(Mono.error(new CandidateException(Constants.NO_CANDIDATE_WITH_EMAIL_FOUND)));

        return candidateDtoMono.flatMap(candidateDto ->
                getProfileImage(candidateDto.getImageName())
                        .map(imageResponse -> createCandidateProfileResponse(imageResponse, candidateDto))
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
                        .map(imageResponse -> createCandidateProfileResponse(imageResponse, candidateDto))
        );
    }


    @Override
    public Mono<UploadImageResponse> uploadCandidateImage(MultipartFile file, Long candidateId) throws IOException {
        return candidateRepository.findById(candidateId)
                .switchIfEmpty(Mono.error(new CandidateException(Constants.NO_CANDIDATE_FOUND_WITH_ID_ + candidateId)))
                .flatMap(candidate -> {
                    Mono<Void> deleteImage = Mono.empty();
                    if (candidate.getImageName() != null && !candidate.getImageName().isEmpty()) {
                        deleteImage = imageStorageRepository.deleteByName(candidate.getImageName());
                    }
                    candidate.setImageName(file.getOriginalFilename());
                    return deleteImage.then(candidateRepository.save(candidate));
                })
                .flatMap(savedCandidate -> {
                    try {
                        return fileStorageService.uploadImage(file)
                                .map(uploadImageResponse -> UploadImageResponse.builder()
                                        .message(uploadImageResponse.getMessage())
                                        .build());
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                })
                .onErrorResume(CandidateException.class, ex -> Mono.just(new UploadImageResponse(ex.getMessage())));
    }

    @Override
    public Mono<ImageResponse> getProfileImage(String fileName) {
        return fileStorageService.downloadImage(fileName)
                .map(dbImageData -> ImageResponse.builder()
                        .message(IMAGE_FOUND)
                        .imageData(dbImageData)
                        .build())
                .switchIfEmpty(Mono.just(ImageResponse.builder()
                        .message(Constants.UPLOAD_IMAGE_ERROR)
                        .build()));
    }
    private CandidateProfileResponse createCandidateProfileResponse(ImageResponse imageResponse, CandidateDto candidateDto) {
        if (imageResponse != null && imageResponse.getMessage().equals(IMAGE_FOUND)) {
            return CandidateProfileResponse.builder()
                    .candidateDto(candidateDto)
                    .profileImage(imageResponse.getImageData())
                    .build();
        } else {
            return CandidateProfileResponse.builder()
                    .candidateDto(candidateDto)
                    .profileImage(null)
                    .build();
        }
    }
}
