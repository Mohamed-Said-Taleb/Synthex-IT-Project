package com.devsling.fr.controller;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.service.CandidateService;
import lombok.RequiredArgsConstructor;
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
    public Flux<CandidateDto> getCandidates(){
        return candidateService.getCandidates();
    }

    @GetMapping("/{id}")
    public Mono<CandidateDto> getCandidate(@PathVariable Long id){
        return candidateService.getCandidate(id);
    }

    @PostMapping
    public Mono<CandidateDto> saveCandidate(@RequestBody CandidateDto candidateDto){
        return candidateService.saveCandidate(Mono.just(candidateDto));
    }

    @PutMapping("/update/{id}")
    public Mono<CandidateDto> updateCandidate(@RequestBody CandidateDto candidateDto,@PathVariable Long id){
        return candidateService.updateCandidate(Mono.just(candidateDto),id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteCandidate(@PathVariable Long id){
        return candidateService.deleteCandidate(id);
    }
}
