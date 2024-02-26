package com.devsling.fr.controller;

import com.devsling.fr.dto.EmployerDto;
import com.devsling.fr.service.EmployerService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employers")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerService employerService;


    @GetMapping("/all")
    public Flux<EmployerDto> getEmployer(){
        return employerService.getEmployer();
    }

    @GetMapping("/{id}")
    public Mono<EmployerDto> getEmployer(@PathVariable Long id){
        return employerService.getEmployerById(id);
    }

    @PostMapping
    public Mono<EmployerDto> saveEmployer(@RequestBody EmployerDto employerDto){
        return employerService.saveEmployer(Mono.just(employerDto));
    }

    @PutMapping("/update/{id}")
    public Mono<EmployerDto> updateEmployer(@RequestBody EmployerDto employerDto, @PathVariable Long id){
        return employerService.updateEmployer(Mono.just(employerDto),id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteEmployerById(@PathVariable Long id){
        return employerService.deleteEmployerById(id);
    }
    @PostMapping("/me")
    public Mono<ResponseEntity<EmployerDto>> getCandidateByEmail(@RequestParam String email) {
        return employerService.getEmployerByEmail(email)
                .map(candidateDto -> ResponseEntity.status(HttpStatus.OK).body(candidateDto));
    }
}
