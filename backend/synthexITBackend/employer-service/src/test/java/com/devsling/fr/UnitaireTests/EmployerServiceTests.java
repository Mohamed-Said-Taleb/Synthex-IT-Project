package com.devsling.fr.UnitaireTests;

import com.devsling.fr.dto.EmployerDto;
import com.devsling.fr.model.Employer;
import com.devsling.fr.repository.EmployerRepository;
import com.devsling.fr.service.EmployerService;
import com.devsling.fr.service.EmployerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployerServiceTests {

    @Mock
    public EmployerService employerService;
    @Mock
    EmployerRepository employerRepository;
    @MockBean
    protected Clock clock;

    @BeforeEach
    public void setup() {

        employerService = new EmployerServiceImpl(employerRepository);
        MockHelper.fixClock(clock, LocalDate.of(2100, 12, 31));
    }

    @Test
    public void testGetEmployers() {
        Employer employer1 = Employer.builder().email("test1@gmail.com").firstName("test1").lastName("test1").build();
        Employer employer2  = Employer.builder().email("test2@gmail.com").firstName("test2").lastName("test2").build();
        when(employerRepository.findAll()).thenReturn(Flux.just(employer1, employer2));
        Flux<EmployerDto> result = employerService.getEmployer();
        Assertions.assertEquals(2, Objects.requireNonNull(result.collectList().block()).size());
    }

    @Test
    public void testGetEmployer() {
        Long id = 1L;
        Employer employer = Employer.builder().email("test@gmail.com").firstName("test").lastName("test").build();
        when(employerRepository.findById(id)).thenReturn(Mono.just(employer));
        Mono<EmployerDto> result = employerService.getEmployer(id);
        Assertions.assertEquals("test", Objects.requireNonNull(result.block()).getFirstName());
    }

    @Test
    public void testSaveEmployer() {
        EmployerDto candidateDto = new EmployerDto("test", "test", "test@gmail.com");
        Employer employer = Employer.builder().email("test@gmail.com").firstName("test").lastName("test").build();
        when(employerRepository.save(any())).thenReturn(Mono.just(employer));

        Mono<EmployerDto> result = employerService.saveEmployer(Mono.just(candidateDto));
        Assertions.assertEquals("test", Objects.requireNonNull(result.block()).getFirstName());
    }

    @Test
    public void testUpdateEmployer() {
        Long id = 1L;
        EmployerDto candidateDto = new EmployerDto("updated", "updated", "updated@gmail.com");
        Employer employer = Employer.builder().email("updated@gmail.com").firstName("updated").lastName("updated").build();
        when(employerRepository.findById(id)).thenReturn(Mono.just(employer));
        when(employerRepository.save(any())).thenReturn(Mono.just(employer));

        Mono<EmployerDto> result = employerService.updateEmployer(Mono.just(candidateDto), id);

        Assertions.assertEquals("updated", Objects.requireNonNull(result.block()).getFirstName());
    }

    @Test
    public void deleteEmployerReturnsEmptyMono() {
        Long candidateId = 1L;
        when(employerRepository.deleteById(candidateId)).thenReturn(Mono.empty());
        Mono<Void> deleteMono = employerService.deleteEmployer(candidateId);
        StepVerifier.create(deleteMono).verifyComplete();
    }

    @Test
    public void testGetEmployerByEmail() {
        String email = "test@gmail.com";
        Employer employer = Employer.builder().email("test@gmail.com").firstName("test").lastName("test").build();
        when(employerRepository.findByEmail(email)).thenReturn(Mono.just(employer));
        Mono<EmployerDto> result = employerService.getEmployerByEmail(email);
        Assertions.assertEquals("test", Objects.requireNonNull(result.block()).getFirstName());
    }
}
