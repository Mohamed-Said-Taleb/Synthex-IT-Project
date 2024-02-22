package com.devsling.fr.service;

import com.devsling.fr.dto.EmployerDto;
import com.devsling.fr.repository.EmployerRepository;
import com.devsling.fr.service.EmployerService;
import com.devsling.fr.tools.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;

    @Override
    public Flux<EmployerDto> getEmployer(){
        return employerRepository.findAll().map(AppUtils::entityToDto);
    }
    @Override
    public Mono<EmployerDto> getEmployer(Long id){
        return employerRepository.findById(id).map(AppUtils::entityToDto);
    }

    @Override
    public Mono<EmployerDto> saveEmployer(Mono<EmployerDto> candidateDtoMono) {
        return candidateDtoMono
                .map(AppUtils::dtoToEntity)
                .flatMap(employerRepository::save)
                .map(AppUtils::entityToDto);
    }
    @Override
    public Mono<EmployerDto> updateEmployer(Mono<EmployerDto> employerDtoMono, Long id){
        return employerRepository.findById(id)
                .flatMap(p->employerDtoMono.map(AppUtils::dtoToEntity)
                        .doOnNext(e->e.setId(id)))
                .flatMap(employerRepository::save)
                .map(AppUtils::entityToDto);

    }
    @Override
    public Mono<Void> deleteEmployer(Long id){
        return employerRepository.deleteById(id);
    }

    @Override
    public Mono<EmployerDto> getEmployerByEmail(String email) {
        return employerRepository.findByEmail(email).map(AppUtils::entityToDto);
    }

}
