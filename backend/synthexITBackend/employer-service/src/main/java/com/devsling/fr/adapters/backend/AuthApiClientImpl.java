package com.devsling.fr.adapters.backend;

import com.devsling.fr.adapters.backend.model.auth.ValidateTokenBackendResponse;
import com.devsling.fr.domain.port.out.AuthApiClient;
import com.devsling.fr.exceptions.AuthException;
import com.devsling.fr.exceptions.ErrorMessage;
import com.devsling.fr.tools.Constants;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AuthApiClientImpl implements AuthApiClient {

    private final WebClient authWebClient;
    private static final String VALIDATION_TOKEN_PATH = "/auth/validate"; //NOSONAR

    @Override
    public Mono<ValidateTokenBackendResponse> getValidationTokenResponse(String accessToken) {
        return authWebClient.post()
                .uri("http://localhost:8083/auth/validate",
                        uriBuilder -> uriBuilder.queryParam("token", accessToken)
                                .build())
                .retrieve()
                .bodyToMono(ValidateTokenBackendResponse.class);
                /**.doOnError(error -> Mono.error(new InternalBFFException(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.SECURE_TOKEN_SELF, error.getMessage(),Constants.AUTH_SERVICE)))
                .onErrorResume(throwable -> throwable instanceof WebClientRequestException && throwable.getCause() instanceof ReadTimeoutException,
                        error -> Mono.error(new GatewayTimeoutException(Constants.SECURE_TOKEN_SELF, Constants.STS_SERVICE, error.getCause())));**/
    }
    private Function<ClientResponse, Mono<? extends Throwable>> errorHandler() {
        return clientResponse -> clientResponse.bodyToMono(ErrorMessage.class)
                .flatMap(errorResponse -> Mono.error(new AuthException(clientResponse.statusCode().value(), Constants.SECURE_TOKEN_SELF, errorResponse)));
    }
}
