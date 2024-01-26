package com.devsling.fr.filters;


import com.devsling.fr.exceptions.AuthErrorMessage;
import com.devsling.fr.exceptions.AuthException;
import com.devsling.fr.tools.JwtUtil;
import com.devsling.fr.tools.ValidateTokenResponse;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;

    private final WebClient.Builder webClientBuilder;

    private final JwtUtil jwtUtil;



    public AuthenticationFilter(RouteValidator validator, WebClient.Builder webClientBuilder, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.webClientBuilder = webClientBuilder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request =null;
            if (validator.isSecured.test(exchange.getRequest())) {
                // Check if the header contains the token
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");

                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }


                    // REST call to AUTH service with WebClient
                    String finalAuthHeader = authHeader;
                    Mono<ValidateTokenResponse> validateTokenResponseMono = webClientBuilder.build().post()
                            .uri("http://localhost:8083/auth/validate",
                                    uriBuilder -> uriBuilder.queryParam("token", finalAuthHeader)
                                            .build())
                            .retrieve()
                            .bodyToMono(ValidateTokenResponse.class);

                    validateTokenResponseMono.map(validateTokenResponse -> {
                        // Additional processing of the ValidateTokenResponse
                        String status = validateTokenResponse.getStatus();

                        // Example: Print the status
                        System.out.println("Status from validateTokenResponse: " + status);

                        // Perform additional logic based on the status or do any other processing
                        if ("Token valid".equals(status)) {
                            // Valid access to candidate
                            System.out.println("Valid access to candidate");
                        } else {
                            // Invalid access
                            throw new RuntimeException("Invalid access");
                        }

                        // You can return something if needed
                        return "Processing completed";
                    }).subscribe(); // Subscribe to trigger the WebClient call
            }
            return chain.filter(exchange);
        });
    }



    public static class Config {

    }
}
