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

    @Test
    public void getEmployersOkTestWithStatus200() {

        Flux<EmployerDto> employerDtoFlux = Flux.just(new EmployerDto("test1", "test1", "test1@gmail.com"),
                new EmployerDto("test2", "test2", "test2"));
        when(employerService.getEmployer()).thenReturn(employerDtoFlux);

        webTestClient.get()
                .uri("/employers/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(EmployerDto.class)
                .isEqualTo(Objects.requireNonNull(employerDtoFlux.collectList().block()));
    }

    @Test
    public void getCandidateByIdOkTestWithStatus200() {

        Long employerId = 1L;
        EmployerDto employerDto = new EmployerDto("test", "test", "test@gmail.com");
        when(employerService.getEmployer(employerId)).thenReturn(Mono.just(employerDto));

        webTestClient.get()
                .uri("/employers/{id}", employerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployerDto.class)
                .isEqualTo(employerDto);
    }

    @Test
    public void saveEmployerOkTestWithStatus200() {

        EmployerDto employerDto = new EmployerDto("test", "test", "test@gmail.com");
        when(employerService.saveEmployer(any())).thenReturn(Mono.just(employerDto));

        webTestClient.post()
                .uri("/employers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(employerDto), EmployerDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployerDto.class)
                .isEqualTo(employerDto);
    }

    @Test
    public void updateEmployerOkTestWithStatus200() {

        Long employerId = 1L;
        EmployerDto employerDto = new EmployerDto("test", "test", "test@gmail.com");
        when(employerService.updateEmployer(any(), eq(employerId))).thenReturn(Mono.just(employerDto));

        webTestClient.put()
                .uri("/employers/update/{id}", employerId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(employerDto), EmployerDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployerDto.class)
                .isEqualTo(employerDto);
    }

    @Test
    public void deleteEmployerOkTestWithStatus200() {

        Long employerId = 1L;
        when(employerService.deleteEmployer(employerId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/employers/delete/{id}", employerId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }
}
