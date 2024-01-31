package com.devsling.fr.domain.port.out;

import com.devsling.fr.adapters.backend.model.auth.ValidateTokenBackendResponse;
import reactor.core.publisher.Mono;

public interface AuthApiClient {
    Mono<ValidateTokenBackendResponse> getValidationTokenResponse(String accessToken);

}
