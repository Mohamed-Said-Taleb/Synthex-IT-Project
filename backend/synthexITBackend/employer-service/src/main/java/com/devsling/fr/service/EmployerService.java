package com.devsling.fr.service;

import com.devsling.fr.dto.EmployerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployerService {
    Flux<EmployerDto> getEmployer();
    Mono<EmployerDto> getEmployer(Long id);
    Mono<EmployerDto> saveEmployer(Mono<EmployerDto> candidateDtoMono);
    Mono<EmployerDto> updateEmployer(Mono<EmployerDto> candidateDtoMono, Long id);
    Mono<Void> deleteEmployer(Long id);

    Mono<EmployerDto> getEmployerByEmail(String email);
}
