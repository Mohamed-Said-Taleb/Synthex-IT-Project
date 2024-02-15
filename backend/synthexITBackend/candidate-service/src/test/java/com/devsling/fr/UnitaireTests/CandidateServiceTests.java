package com.devsling.fr.UnitaireTests;

import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.model.Candidate;
import com.devsling.fr.repository.CandidateRepository;
import com.devsling.fr.service.CandidateService;
import com.devsling.fr.service.Impl.CandidateServiceImpl;
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
public class CandidateServiceTests {

    @Mock
    public CandidateService candidateService;
    @Mock
    CandidateRepository candidateRepository;
    @MockBean
    protected Clock clock;

    @BeforeEach
    public void setup() {

        candidateService = new CandidateServiceImpl(candidateRepository);
        MockHelper.fixClock(clock, LocalDate.of(2100, 12, 31));
    }

    @Test
    public void testGetCandidates() {
        Candidate candidate1 = Candidate.builder().email("test1@gmail.com").firstName("test1").lastName("test1").build();
        Candidate candidate2 = candidate2 = Candidate.builder().email("test2@gmail.com").firstName("test2").lastName("test2").build();
        when(candidateRepository.findAll()).thenReturn(Flux.just(candidate1, candidate2));
        Flux<CandidateDto> result = candidateService.getCandidates();
        Assertions.assertEquals(2, Objects.requireNonNull(result.collectList().block()).size());
    }

    @Test
    public void testGetCandidate() {
        Long id = 1L;
        Candidate candidate = Candidate.builder().email("test@gmail.com").firstName("test").lastName("test").build();
        when(candidateRepository.findById(id)).thenReturn(Mono.just(candidate));
        Mono<CandidateDto> result = candidateService.getCandidate(id);
        Assertions.assertEquals("test", Objects.requireNonNull(result.block()).getFirstName());
    }

    @Test
    public void testSaveCandidate() {
        CandidateDto candidateDto = CandidateDto.builder()
                .email("test@gmail.com").build();
        Candidate candidate = Candidate.builder().email("test@gmail.com").firstName("test").lastName("test").build();
        when(candidateRepository.save(any())).thenReturn(Mono.just(candidate));

        Mono<CandidateDto> result = candidateService.saveCandidate(Mono.just(candidateDto));
        Assertions.assertEquals("test", Objects.requireNonNull(result.block()).getFirstName());
    }

    @Test
    public void testUpdateCandidate() {
        Long candidateId = 1L;


        CandidateDto candidateDto = CandidateDto.builder()
                .email("test@gmail.com")
                .firstName("updated")
                .lastName("updated")
                .build();
        Candidate candidate = Candidate.builder().email("updated@gmail.com").firstName("updated").lastName("updated").build();
        when(candidateRepository.findById(candidateId)).thenReturn(Mono.just(candidate));
        when(candidateRepository.save(any())).thenReturn(Mono.just(candidate));

        Mono<CandidateDto> result = candidateService.updateCandidate(Mono.just(candidateDto), candidateId);

        Assertions.assertEquals("updated", Objects.requireNonNull(result.block()).getFirstName());
    }

    @Test
    public void deleteCandidateReturnsEmptyMono() {
        Long candidateId = 1L;
        when(candidateRepository.deleteById(candidateId)).thenReturn(Mono.empty());
        Mono<Void> deleteMono = candidateService.deleteCandidate(candidateId);
        StepVerifier.create(deleteMono).verifyComplete();
    }

    @Test
    public void testGetCandidateByEmail() {
        String email = "test@gmail.com";
        Candidate candidate = Candidate.builder().email("test@gmail.com").firstName("test").lastName("test").build();
        when(candidateRepository.findByEmail(email)).thenReturn(Mono.just(candidate));
        Mono<CandidateDto> result = candidateService.getCandidateByEmail(email);
        Assertions.assertEquals("test", Objects.requireNonNull(result.block()).getFirstName());
    }
}
