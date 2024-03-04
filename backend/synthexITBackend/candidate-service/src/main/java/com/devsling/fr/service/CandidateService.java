package com.devsling.fr.service;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.dto.CandidateProfileResponse;
import com.devsling.fr.dto.FileUploadResponse;
import com.devsling.fr.dto.ImageResponse;
import com.devsling.fr.dto.UploadImageResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

public interface CandidateService {
    Flux<CandidateProfileResponse> getCandidates();
    Mono<CandidateProfileResponse> getCandidateById(Long id);
    Mono<CandidateDto> saveCandidate(Mono<CandidateDto> candidateDtoMono);
    Mono<CandidateDto> updateCandidate(Mono<CandidateDto> candidateDtoMono, Long id);
    Mono<Void> deleteCandidateById(Long id);
    Mono<CandidateProfileResponse> getCandidateProfile(String email);
    Mono<UploadImageResponse> uploadCandidateImage(MultipartFile file,Long candidateId) throws IOException;
    Mono<ImageResponse>  getProfileImage(String fileName);
    Mono<FileUploadResponse> uploadCandidateResume(MultipartFile file, Long candidateId) throws IOException;
    Mono<Void> deleteResume(String filename,Long candidateId);
    Mono<Resource> downloadFile(String fileCode);

}
