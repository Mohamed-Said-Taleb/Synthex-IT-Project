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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    public void testGetEmployersReturn200_Ok() {

        Employer employer1 = Employer.builder()
                .email("test1@gmail.com")
                .build();
        Employer employer2  = Employer.builder()
                .email("test2@gmail.com")
                .build();

        when(employerRepository.findAll()).thenReturn(Flux.just(employer1, employer2));

        Flux<EmployerDto> result = employerService.getEmployer();
        Assertions.assertEquals(2, Objects.requireNonNull(result.collectList().block()).size());
    }

    @Test
    public void testGetEmployerByIdReturn200_Ok() {

        Long id = 1L;
        Employer employer = Employer.builder()
                .email("test@gmail.com")
                .build();

        when(employerRepository.findById(id)).thenReturn(Mono.just(employer));

        Mono<EmployerDto> result = employerService.getEmployerById(id);
        Assertions.assertEquals("test@gmail.com", Objects.requireNonNull(result.block()).getEmail());
    }

    @Test
    public void testSaveEmployerReturn200_Ok() {

        EmployerDto employerDto = EmployerDto.builder()
                .email("test@gmail.com")
                .build();

        Employer employer = Employer.builder()
                .email("test@gmail.com")
                .build();
        when(employerRepository.save(any())).thenReturn(Mono.just(employer));

        Mono<EmployerDto> result = employerService.saveEmployer(Mono.just(employerDto));
        Assertions.assertEquals("test@gmail.com", Objects.requireNonNull(result.block()).getEmail());
    }

    @Test
    public void testUpdateEmployerReturn_200_Ok() {

        Long id = 1L;
        EmployerDto employerDto = EmployerDto.builder()
                .email("test@gmail.com")
                .build();

        Employer employer = Employer.builder()
                .email("test@gmail.com")
                .build();

        when(employerRepository.findById(id)).thenReturn(Mono.just(employer));
        when(employerRepository.save(any())).thenReturn(Mono.just(employer));

        Mono<EmployerDto> result = employerService.updateEmployer(Mono.just(employerDto), id);

        Assertions.assertEquals("test@gmail.com", Objects.requireNonNull(result.block()).getEmail());
    }


    @Test
    public void deleteEmployerById_Success() {
        // Arrange
        Long employerId = 1L;
        when(employerRepository.deleteById(employerId)).thenReturn(Mono.empty());

        // Act
        Mono<Void> deleteMono = employerService.deleteEmployerById(employerId);

        // Assert
        StepVerifier.create(deleteMono).verifyComplete();
        verify(employerRepository, times(1)).deleteById(employerId);
    }
    @Test
    public void deleteEmployerById_Failure() {
        Long employerId = 1L;
        when(employerRepository.deleteById(employerId)).thenReturn(Mono.error(new RuntimeException("Delete failed")));

        Mono<Void> deleteMono = employerService.deleteEmployerById(employerId);

        StepVerifier.create(deleteMono)
                .expectError(RuntimeException.class)
                .verify();
    }
    @Test
    public void testGetEmployerByEmail() {
        String email = "test@gmail.com";
        Employer employer = Employer.builder()
                .email("test@gmail.com")
                .build();
        when(employerRepository.findByEmail(email)).thenReturn(Mono.just(employer));
        Mono<EmployerDto> result = employerService.getEmployerByEmail(email);
        Assertions.assertEquals("test@gmail.com", Objects.requireNonNull(result.block()).getEmail());
    }
}
