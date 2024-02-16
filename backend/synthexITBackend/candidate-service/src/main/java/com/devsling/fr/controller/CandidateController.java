package com.devsling.fr.controller;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.exceptions.ErrorException;
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
                .map(candidateDto -> ResponseEntity.status(HttpStatus.OK).body(candidateDto));
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<CandidateDto>> getCandidate(@PathVariable Long id) {
        return candidateService.getCandidate(id)
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
        return candidateService.deleteCandidate(id)
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
    @GetMapping("/profile")
    public Mono<ResponseEntity<CandidateDto>> getCandidateByEmail(@RequestBody String email) {
        return candidateService.getCandidateByEmail(email)
                .map(candidateDto -> ResponseEntity.status(HttpStatus.OK).body(candidateDto));
    }

}
