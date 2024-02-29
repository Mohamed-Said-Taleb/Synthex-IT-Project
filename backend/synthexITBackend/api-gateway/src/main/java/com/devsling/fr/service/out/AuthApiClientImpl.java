package com.devsling.fr.service.out;

import com.devsling.fr.exceptions.GatewayTimeoutException;
import com.devsling.fr.service.AuthApiClient;
import com.devsling.fr.tools.Constants;
import com.devsling.fr.dto.ValidateTokenResponse;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import static com.devsling.fr.tools.Constants.TOKEN;

@Service
@RequiredArgsConstructor
public class AuthApiClientImpl implements AuthApiClient {

    private final WebClient authWebClient ;

    private final static String VALIDATE_TOKEN_PATH="/auth/validate-token";
    @Override
    public Mono<ValidateTokenResponse> validateToken(String token) {
       return authWebClient.post()
                .uri(VALIDATE_TOKEN_PATH,
                        uriBuilder -> uriBuilder.queryParam(TOKEN, token)
                                .build())
                .retrieve()
                .bodyToMono(ValidateTokenResponse.class)
               .onErrorResume(throwable -> throwable instanceof WebClientRequestException && throwable.getCause() instanceof ReadTimeoutException,
                       error -> Mono.error(new GatewayTimeoutException(Constants.AUTH_SERVICE_NAME,
                               Constants.AUTH_SERVICE_TOKEN_VALIDATION,error.getMessage(), HttpStatus.GATEWAY_TIMEOUT)));
    }
}
