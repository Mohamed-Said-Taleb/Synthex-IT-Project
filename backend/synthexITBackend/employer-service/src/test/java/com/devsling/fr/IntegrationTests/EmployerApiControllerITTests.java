package com.devsling.fr.IntegrationTests;

import com.devsling.fr.controller.EmployerController;
import com.devsling.fr.dto.EmployerDto;
import com.devsling.fr.service.EmployerService;
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
@WebFluxTest(EmployerController.class)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class EmployerApiControllerITTests {


    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployerService employerService;

    private final static String BASIC_PATH="/employers";

    @Test
    public void getCandidateByIdOkTestWithStatus200() {

        Long employerId = 1L;
        EmployerDto employerDto = EmployerDto.builder()
                .email("test@gmail.com")
                .build();

        when(employerService.getEmployerById(employerId)).thenReturn(Mono.just(employerDto));

        webTestClient.get()
                .uri(BASIC_PATH+"/{id}", employerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(EmployerDto.class)
                .isEqualTo(employerDto);
    }

    @Test
    public void saveEmployerOkTestWithStatus200() {

        EmployerDto employerDto = EmployerDto.builder()
                .email("test@gmail.com")
                .build();

        when(employerService.saveEmployer(any())).thenReturn(Mono.just(employerDto));

        webTestClient.post()
                .uri(BASIC_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(employerDto), EmployerDto.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(EmployerDto.class)
                .isEqualTo(employerDto);
    }

    @Test
    public void updateEmployerOkTestWithStatus200() {

        Long employerId = 1L;
        EmployerDto employerDto = EmployerDto.builder()
                .email("test@gmail.com")
                .build();

        when(employerService.updateEmployer(any(), eq(employerId))).thenReturn(Mono.just(employerDto));

        webTestClient.put()
                .uri(BASIC_PATH+"/update/{id}", employerId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(employerDto), EmployerDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(EmployerDto.class)
                .isEqualTo(employerDto);
    }

    @Test
    public void deleteEmployerOkTestWithStatus200() {

        Long employerId = 1L;
        when(employerService.deleteEmployerById(employerId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(BASIC_PATH+"/delete/{id}", employerId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Void.class);
    }
}
