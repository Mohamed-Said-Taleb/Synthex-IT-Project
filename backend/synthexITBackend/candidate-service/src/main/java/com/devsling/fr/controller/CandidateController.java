package com.devsling.fr.controller;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.dto.ErrorMessageResponse;
import com.devsling.fr.service.CandidateService;
import com.devsling.fr.tools.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;


    @GetMapping("all")
    public Flux<ResponseEntity<CandidateDto>> getCandidates() {
        return candidateService.getCandidates()
                .map(candidateDto -> ResponseEntity.status(HttpStatus.OK).body(candidateDto))
                .onErrorResume(throwable -> {
                    CandidateDto errorDto = CandidateDto.builder()
                            .errorResponse(ErrorMessageResponse.builder()
                                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .message(Constants.GET_CANDIDATE_ERROR_MESSAGE)
                                    .service(Constants.CANDIDATE_SERVICE_NAME)
                                    .build())
                            .build();
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto));
                });
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<CandidateDto>> getCandidate(@PathVariable Long id) {
        return candidateService.getCandidate(id)
                .map(candidateDto -> ResponseEntity.status(HttpStatus.OK).body(candidateDto))
                .onErrorResume(throwable -> {
                    CandidateDto errorDto = CandidateDto.builder()
                            .errorResponse(ErrorMessageResponse.builder()
                                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .message(Constants.GET_CANDIDATE_ERROR_MESSAGE)
                                    .service(Constants.CANDIDATE_SERVICE_NAME)
                                    .build())
                            .build();
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto));
                });
    }

    @PostMapping
    public Mono<ResponseEntity<CandidateDto>> saveCandidate(@RequestBody CandidateDto candidateDto) {
        return candidateService.saveCandidate(Mono.just(candidateDto))
                .map(savedCandidateDto -> ResponseEntity.status(HttpStatus.CREATED).body(savedCandidateDto))
                .onErrorResume(error -> {
                    ErrorMessageResponse errorMessageResponse = ErrorMessageResponse.builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(Constants.SAVE_CANDIDATE_ERROR_MESSAGE)
                            .service(Constants.CANDIDATE_SERVICE_NAME)
                            .build();
                    CandidateDto errorCandidateDto = CandidateDto.builder()
                            .errorResponse(errorMessageResponse)
                            .build();
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCandidateDto));
                });
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<CandidateDto>> updateCandidate(@RequestBody CandidateDto candidateDto, @PathVariable Long id) {
        return candidateService.updateCandidate(Mono.just(candidateDto), id)
                .map(updatedCandidateDto -> ResponseEntity.status(HttpStatus.OK).body(updatedCandidateDto))
                .onErrorResume(throwable -> {
                    ErrorMessageResponse errorMessageResponse = ErrorMessageResponse.builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(Constants.UPDATE_CANDIDATE_ERROR_MESSAGE)
                            .service(Constants.CANDIDATE_SERVICE_NAME)
                            .build();
                    CandidateDto errorCandidateDto = CandidateDto.builder()
                            .errorResponse(errorMessageResponse)
                            .build();
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCandidateDto));
                });
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Object>> deleteCandidate(@PathVariable Long id) {
        return candidateService.deleteCandidate(id)
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build())
                .onErrorResume(throwable -> {
                    ErrorMessageResponse errorMessageResponse = ErrorMessageResponse.builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(Constants.DELETE_CANDIDATE_ERROR_MESSAGE)
                            .service(Constants.CANDIDATE_SERVICE_NAME)
                            .build();
                    CandidateDto errorCandidateDto = CandidateDto.builder()
                            .errorResponse(errorMessageResponse)
                            .build();
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCandidateDto));
                });
    }
}
