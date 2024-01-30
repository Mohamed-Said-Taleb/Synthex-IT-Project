package com.devsling.fr.filters;

import com.devsling.fr.tools.JwtUtil;
import com.devsling.fr.tools.ValidateTokenResponse;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;

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
            if (validator.isSecured.test(exchange.getRequest())) {
                // Check if the header contains the token
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                String finalAuthHeader = authHeader;
                return webClientBuilder.build().post()
                        .uri("http://localhost:8083/auth/validate",
                                uriBuilder -> uriBuilder.queryParam("token", finalAuthHeader).build())
                        .retrieve()
                        .bodyToMono(ValidateTokenResponse.class)
                        .flatMap(validateTokenResponse -> {
                            // Additional processing of the ValidateTokenResponse
                            String status = validateTokenResponse.getStatus();

                            if ("Valid".equals(status)) {
                                // Valid access to candidate
                                return chain.filter(exchange);
                            } else {
                                // Invalid access, do not redirect to candidate microservice
                                return Mono.error(new AuthenticationException("Not Authorized"));
                            }
                        })
                        .onErrorResume(WebClientRequestException.class, e -> {
                            // Handle WebClientRequestException (e.g., if auth service is unreachable)
                            return Mono.error(new RuntimeException("Error calling auth service", e));
                        });
            }else {
                return chain.filter(exchange);
            }

        });
    }




    public static class Config {

    }
}
