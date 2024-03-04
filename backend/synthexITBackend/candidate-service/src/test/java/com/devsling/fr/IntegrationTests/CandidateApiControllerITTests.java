package com.devsling.fr.IntegrationTests;

import com.devsling.fr.controller.CandidateController;
import com.devsling.fr.dto.CandidateDto;
import com.devsling.fr.dto.CandidateProfileResponse;
import com.devsling.fr.exceptions.CandidateException;
import com.devsling.fr.service.CandidateService;
import com.devsling.fr.tools.Constants;
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

    private final static String BASIC_PATH="/candidates";

    @Test
    void getCandidates_NoCandidatesFound_ReturnsError() {
        // Arrange
        when(candidateService.getCandidates()).thenReturn(Flux.error(new CandidateException(Constants.NO_CANDIDATE_FOUND)));

        // Act & Assert
        webTestClient.get()
                .uri(BASIC_PATH+"/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void getCandidates_CandidatesFound_ReturnsCandidates() {

        CandidateDto candidate1 =  CandidateDto.builder()
                .build();
        CandidateDto candidate2 = CandidateDto.builder()
                .build();

        CandidateProfileResponse candidateProfileResponse1 = CandidateProfileResponse.builder()
                .candidateDto(candidate1)
                .build();
        CandidateProfileResponse candidateProfileResponse2 = CandidateProfileResponse.builder()
                .candidateDto(candidate2)
                .build();
        when(candidateService.getCandidates()).thenReturn(Flux.just(candidateProfileResponse1, candidateProfileResponse2));

        webTestClient.get()
                .uri(BASIC_PATH+"/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CandidateDto.class)
                .hasSize(2)
                .contains(candidate1, candidate2);
    }
    @Test
    public void getCandidateByIdOkTestWithStatus200() {

        long candidateId = 1L;
        CandidateDto candidateDto = CandidateDto.builder()
                .build();
        CandidateProfileResponse candidateProfileResponse = CandidateProfileResponse.builder()
                .candidateDto(candidateDto)
                .build();

        when(candidateService.getCandidateById(candidateId)).thenReturn(Mono.just(candidateProfileResponse));

        webTestClient.get()
                .uri(BASIC_PATH+"/{id}", candidateId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CandidateDto.class)
                .isEqualTo(candidateDto);
    }

    @Test
    public void saveCandidateOkTestWithStatus200() {

        CandidateDto candidateDto = CandidateDto.builder()
                .email("test@gmail.com")
                .build();
        when(candidateService.saveCandidate(any())).thenReturn(Mono.just(candidateDto));

        webTestClient.post()
                .uri(BASIC_PATH)
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
                .email("test@gmail.com")
                .build();
        when(candidateService.updateCandidate(any(), eq(candidateId))).thenReturn(Mono.just(candidateDto));

        webTestClient.put()
                .uri(BASIC_PATH+"/update/{id}", candidateId)
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
        when(candidateService.deleteCandidateById(candidateId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(BASIC_PATH+"/delete/{id}", candidateId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Void.class);
    }
}
