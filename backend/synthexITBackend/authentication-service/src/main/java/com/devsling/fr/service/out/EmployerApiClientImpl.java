package com.devsling.fr.service.out;

import com.devsling.fr.dto.Requests.EmployerCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EmployerApiClientImpl implements EmployerApiClient {
    private static final String SAVE_EMPLOYER_PATH = "/employers";

    private final WebClient employerWebClient;

    @Override
    public Mono<EmployerCreateRequest> saveEmployer(EmployerCreateRequest employerCreateRequest) {
        return employerWebClient.post()
                .uri(SAVE_EMPLOYER_PATH)
                .body(BodyInserters.fromValue(employerCreateRequest))
                .retrieve()
                .bodyToMono(EmployerCreateRequest.class)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error calling candidate microservice")));
    }
}
