package com.devsling.fr.service;

import com.devsling.fr.tools.ValidateTokenResponse;
import reactor.core.publisher.Mono;

public interface AuthApiClient {
    Mono<ValidateTokenResponse> validateToken(String token);
}
