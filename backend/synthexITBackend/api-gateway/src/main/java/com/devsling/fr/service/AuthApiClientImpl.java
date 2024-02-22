package com.devsling.fr.service;

import com.devsling.fr.tools.ValidateTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.devsling.fr.tools.Constants.TOKEN;

@Service
@RequiredArgsConstructor
public class AuthApiClientImpl implements AuthApiClient{

    private final WebClient authWebClient ;

    private final static String VALIDATE_TOKEN_PATH="/auth/validate-token";
    @Override
    public Mono<ValidateTokenResponse> validateToken(String token) {
       return authWebClient.post()
                .uri(VALIDATE_TOKEN_PATH,
                        uriBuilder -> uriBuilder.queryParam(TOKEN, token)
                                .build())
                .retrieve()
                .bodyToMono(ValidateTokenResponse.class);
    }
}
