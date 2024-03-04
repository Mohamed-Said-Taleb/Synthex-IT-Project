package com.devsling.fr.service;

import com.devsling.fr.dto.EmployerDto;
import com.devsling.fr.exceptions.EmployerException;
import com.devsling.fr.repository.EmployerRepository;
import com.devsling.fr.tools.EmployerMapper;
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
    public Flux<EmployerDto> getEmployer() {
        return employerRepository.findAll()
                .map(EmployerMapper::entityToDto)
                .collectList()
                .flatMapMany(list -> {
                    if (list.isEmpty()) {
                        return Flux.error(new EmployerException(Constants.NO_EMPLOYER_FOUND));
                    } else {
                        return Flux.fromIterable(list);
                    }
                });
    }
    @Override
    public Mono<EmployerDto> getEmployerById(Long id){
        return employerRepository.findById(id).map(EmployerMapper::entityToDto)
                .switchIfEmpty(Mono.error(new EmployerException(Constants.NO_EMPLOYER_FOUND_WITH_ID_+id)));

    }

    @Override
    public Mono<EmployerDto> saveEmployer(Mono<EmployerDto> candidateDtoMono) {
        return candidateDtoMono
                .map(EmployerMapper::dtoToEntity)
                .flatMap(employerRepository::save)
                .map(EmployerMapper::entityToDto)
                .switchIfEmpty(Mono.error(new EmployerException(Constants.SAVE_EMPLOYER_ERROR_MESSAGE)));
    }
    @Override
    public Mono<EmployerDto> updateEmployer(Mono<EmployerDto> employerDtoMono, Long id){
        return employerRepository.findById(id)
                .flatMap(existingEmployer -> employerDtoMono.map(updatedEmployerDto -> {
                    if (updatedEmployerDto.getCompanyName() != null) {
                        existingEmployer.setCompanyName(updatedEmployerDto.getCompanyName());
                    }
                    if (updatedEmployerDto.getSector() != null) {
                        existingEmployer.setCompanyName(updatedEmployerDto.getSector());
                    }
                    if (updatedEmployerDto.getAddress() != null) {
                        existingEmployer.setCompanyName(updatedEmployerDto.getAddress());
                    }
                    if (updatedEmployerDto.getPhoneNumber() != null) {
                        existingEmployer.setCompanyName(updatedEmployerDto.getPhoneNumber());
                    }
                    return existingEmployer;
                }))
                .flatMap(employerRepository::save)
                .map(EmployerMapper::entityToDto)
                .switchIfEmpty(Mono.error(new EmployerException(Constants.UPDATE_EMPLOYER_ERROR_MESSAGE)));
    }
    @Override
    public Mono<Void> deleteEmployerById(Long id){
        return employerRepository.deleteById(id)
                .switchIfEmpty(Mono.error(new EmployerException(Constants.DELETE_EMPLOYER_ERROR_MESSAGE)));
    }

    @Override
    public Mono<EmployerDto> getEmployerByEmail(String email) {
        return employerRepository.findByEmail(email).map(EmployerMapper::entityToDto)
                .switchIfEmpty(Mono.error(new EmployerException(Constants.NO_EMPLOYER_WITH_EMAIL_FOUND)));

    }

}
