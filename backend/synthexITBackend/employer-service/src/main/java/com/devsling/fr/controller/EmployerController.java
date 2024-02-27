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
    public Flux<ResponseEntity<EmployerDto>>getEmployer(){
        return employerService.getEmployer()
                .map(employerDto -> ResponseEntity.status(HttpStatus.OK).body(employerDto));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployerDto>> getEmployer(@PathVariable Long id){
        return employerService.getEmployerById(id)
                .map(employerDto -> ResponseEntity.status(HttpStatus.OK).body(employerDto));
    }

    @PostMapping
    public Mono<ResponseEntity<EmployerDto>> saveEmployer(@RequestBody EmployerDto employerDto){
        return employerService.saveEmployer(Mono.just(employerDto))
                .map(savedEmployer -> ResponseEntity.status(HttpStatus.CREATED).body(savedEmployer));
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<EmployerDto>> updateEmployer(@RequestBody EmployerDto employerDto, @PathVariable Long id){
        return employerService.updateEmployer(Mono.just(employerDto),id)
                .map(updatedEmployer -> ResponseEntity.status(HttpStatus.OK).body(updatedEmployer));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteEmployerById(@PathVariable Long id){
        return employerService.deleteEmployerById(id)
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());
    }
    @PostMapping("/me")
    public Mono<ResponseEntity<EmployerDto>> getCandidateByEmail(@RequestParam String email) {
        return employerService.getEmployerByEmail(email)
                .map(employerDto -> ResponseEntity.status(HttpStatus.OK).body(employerDto));
    }
}
