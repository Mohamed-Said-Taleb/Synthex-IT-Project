package com.devsling.fr.service.out;

import com.devsling.fr.dto.requests.CandidateRequest;
import com.devsling.fr.dto.responses.CandidateProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CandidateApiClientImpl implements CandidateApiClient{

    private static final String SAVE_CANDIDATE_PATH="/candidates";
    private static final String PROFILE_CANDIDATE_PATH="/candidates/profile";


    private final WebClient candidateWebClient;
    @Override
    public Mono<CandidateProfileResponse> saveCandidate(CandidateRequest candidateRequest) {
        return candidateWebClient.post()
                .uri(SAVE_CANDIDATE_PATH)
                .body(BodyInserters.fromValue(candidateRequest))
                .retrieve()
                .bodyToMono(CandidateProfileResponse.class)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error calling candidate microservice")));
    }
    @Override
    public Mono<CandidateProfileResponse> profileCandidate(String email) {
        return candidateWebClient.post()
                .uri(PROFILE_CANDIDATE_PATH)
                .body(BodyInserters.fromValue(email))
                .retrieve()
                .bodyToMono(CandidateProfileResponse.class)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error calling candidate microservice")));
    }

}
