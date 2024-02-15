package com.devsling.fr.IntegrationTests;

import com.devsling.fr.controller.CandidateController;
import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.service.CandidateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(CandidateController.class)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class CandidateApiControllerITTests {


    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CandidateService candidateService;

    @Test
    public void getCandidatesOkTestWithStatus200() {

        Flux<CandidateDto> candidateDtoFlux = Flux.just(CandidateDto.builder()
                .email("test1@gmail.com").build(),
                CandidateDto.builder()
                        .email("test@gmail.com").build());
        when(candidateService.getCandidates()).thenReturn(candidateDtoFlux);

        webTestClient.get()
                .uri("/candidates/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CandidateDto.class)
                .isEqualTo(Objects.requireNonNull(candidateDtoFlux.collectList().block()));
    }

    @Test
    public void getCandidateByIdOkTestWithStatus200() {

        long candidateId = 1L;
        CandidateDto candidateDto = CandidateDto.builder()
                .email("test@gmail.com").build();
        when(candidateService.getCandidate(candidateId)).thenReturn(Mono.just(candidateDto));

        webTestClient.get()
                .uri("/candidates/{id}", candidateId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CandidateDto.class)
                .isEqualTo(candidateDto);
    }

    @Test
    public void saveCandidateOkTestWithStatus200() {

        CandidateDto candidateDto = CandidateDto.builder()
                .email("test@gmail.com").build();
        when(candidateService.saveCandidate(any())).thenReturn(Mono.just(candidateDto));

        webTestClient.post()
                .uri("/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(candidateDto), CandidateDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CandidateDto.class)
                .isEqualTo(candidateDto);
    }

    @Test
    public void updateCandidateOkTestWithStatus200() {

        long candidateId = 1L;
        CandidateDto candidateDto = CandidateDto.builder()
                .email("test@gmail.com").build();
        when(candidateService.updateCandidate(any(), eq(candidateId))).thenReturn(Mono.just(candidateDto));

        webTestClient.put()
                .uri("/candidates/update/{id}", candidateId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(candidateDto), CandidateDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CandidateDto.class)
                .isEqualTo(candidateDto);
    }

    @Test
    public void deleteCandidateOkTestWithStatus200() {

        long candidateId = 1L;
        when(candidateService.deleteCandidate(candidateId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/candidates/delete/{id}", candidateId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }
}
