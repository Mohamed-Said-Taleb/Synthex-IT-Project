package com.devsling.fr.controller;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.dto.CandidateProfileResponse;
import com.devsling.fr.dto.FileUploadResponse;
import com.devsling.fr.dto.ImageResponse;
import com.devsling.fr.dto.UploadImageResponse;
import com.devsling.fr.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;


    @GetMapping("/all")
    public Flux<CandidateProfileResponse> getCandidates() {
        return candidateService.getCandidates();
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CandidateProfileResponse>> getCandidateById(@PathVariable Long id) {
        return candidateService.getCandidateById(id)
                .map(candidateDto -> ResponseEntity.status(HttpStatus.OK).body(candidateDto));
    }
    @PostMapping
    public Mono<ResponseEntity<CandidateDto>> saveCandidate(@RequestBody CandidateDto candidateDto) {
        return candidateService.saveCandidate(Mono.just(candidateDto))
                .map(savedCandidateDto -> ResponseEntity.status(HttpStatus.CREATED).body(savedCandidateDto));
    }
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<CandidateDto>> updateCandidate(@RequestBody CandidateDto candidateDto, @PathVariable Long id) {
        return candidateService.updateCandidate(Mono.just(candidateDto), id)
                .map(updatedCandidateDto -> ResponseEntity.status(HttpStatus.OK).body(updatedCandidateDto));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Object>> deleteCandidate(@PathVariable Long id) {
        return candidateService.deleteCandidateById(id)
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());
    }
    @PostMapping("/me")
    public Mono<ResponseEntity<CandidateProfileResponse>> getProfileCandidate(@RequestParam String email) {
        return candidateService.getCandidateProfile(email)
                .map(candidateDto -> ResponseEntity.status(HttpStatus.OK)
                        .body(candidateDto));
    }

    @PostMapping("/images/upload")
    public Mono<ResponseEntity<UploadImageResponse>> uploadProfileImage(@RequestParam("image")MultipartFile file,
                                                                 @RequestParam("candidateId") Long candidateId) throws IOException {
      return candidateService.uploadCandidateImage(file,candidateId)
        .map(uploadImageResponse -> ResponseEntity.status(HttpStatus.OK).body(uploadImageResponse));
    }
    @GetMapping("/images/{fileName}")
    public Mono<ResponseEntity<ImageResponse>> downloadProfileImage(@PathVariable String fileName) {
        return candidateService.getProfileImage(fileName)
                .map(imageData -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(imageData));
    }

    @PostMapping("/files/upload")
    public Mono<ResponseEntity<FileUploadResponse>> uploadFile(@RequestParam("file") MultipartFile multipartFile,@RequestParam("candidateId") Long candidateId) throws IOException {
      return candidateService.uploadCandidateResume(multipartFile,candidateId).map(fileUploadResponse -> ResponseEntity.ok()
                .body(fileUploadResponse));
    }
    @GetMapping("/files/download")
    public Mono<ResponseEntity<Resource>> downloadFile(@RequestParam("filename") String fileName) {
        return candidateService.downloadFile(fileName).map(resource -> {
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);
        }).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/files/delete/{id}")
    public Mono<ResponseEntity<Resource>> deleteFile(@PathVariable Long id,@RequestParam("filename") String fileName) {
        return candidateService.deleteResume(fileName,id)
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());
    }
}
