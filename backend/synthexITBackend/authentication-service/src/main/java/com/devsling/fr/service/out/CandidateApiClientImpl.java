package com.devsling.fr.service.out;

import com.devsling.fr.dto.Requests.CandidateRequest;
import com.devsling.fr.dto.Responses.CandidateCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
public class CandidateApiClientImpl implements CandidateApiClient{

    private static final String SAVE_CANDIDATE_PATH="/candidates";

    private final WebClient candidateWebClient;
    @Override
    public Mono<CandidateCreateResponse> saveCandidate(CandidateRequest candidateRequest) {
        return candidateWebClient.post()
                .uri(SAVE_CANDIDATE_PATH)
                .body(BodyInserters.fromValue(candidateRequest))
                .retrieve()
                .bodyToMono(CandidateCreateResponse.class)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error calling candidate microservice")));
    }
}
