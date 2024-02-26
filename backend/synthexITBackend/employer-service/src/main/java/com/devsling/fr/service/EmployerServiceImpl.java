package com.devsling.fr.service;

import com.devsling.fr.dto.EmployerDto;
import com.devsling.fr.exceptions.EmployerException;
import com.devsling.fr.repository.EmployerRepository;
import com.devsling.fr.tools.AppUtils;
import com.devsling.fr.tools.Constants;
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
        return employerRepository.findAll().map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new EmployerException(Constants.NO_EMPLOYER_FOUND)));

    }
    @Override
    public Mono<EmployerDto> getEmployerById(Long id){
        return employerRepository.findById(id).map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new EmployerException(Constants.NO_EMPLOYER_FOUND_WITH_ID_+id)));

    }

    @Override
    public Mono<EmployerDto> saveEmployer(Mono<EmployerDto> candidateDtoMono) {
        return candidateDtoMono
                .map(AppUtils::dtoToEntity)
                .flatMap(employerRepository::save)
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new EmployerException(Constants.SAVE_EMPLOYER_ERROR_MESSAGE)));
    }
    @Override
    public Mono<EmployerDto> updateEmployer(Mono<EmployerDto> employerDtoMono, Long id){
        return employerRepository.findById(id)
                .flatMap(p->employerDtoMono.map(AppUtils::dtoToEntity)
                        .doOnNext(e->e.setId(id)))
                .flatMap(employerRepository::save)
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new EmployerException(Constants.UPDATE_EMPLOYER_ERROR_MESSAGE)));


    }
    @Override
    public Mono<Void> deleteEmployerById(Long id){
        return employerRepository.findById(id)
                .flatMap(employer -> {
                    if (employer != null) {
                        return employerRepository.deleteById(id);
                    } else {
                        return Mono.error(new EmployerException(Constants.DELETE_EMPLOYER_ERROR_MESSAGE));
                    }
                });

    }

    @Override
    public Mono<EmployerDto> getEmployerByEmail(String email) {
        return employerRepository.findByEmail(email).map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new EmployerException(Constants.NO_EMPLOYER_WITH_EMAIL_FOUND)));

    }

}
