package com.devsling.fr.filters;

import com.devsling.fr.tools.JwtUtil;
import com.devsling.fr.tools.ValidateTokenResponse;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;

    private final WebClient authWebClient;

    private final JwtUtil jwtUtil;



    public AuthenticationFilter(RouteValidator validator, WebClient authWebClient, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.authWebClient = authWebClient;
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
                return authWebClient.post()
                        .uri("http://localhost:8083/auth/validate",
                                uriBuilder -> uriBuilder.queryParam("token", finalAuthHeader).build())
                        .retrieve()
                        .bodyToMono(ValidateTokenResponse.class)
                        .flatMap(validateTokenResponse -> {
                            String status = validateTokenResponse.getStatus();

                            if ("Valid".equals(status)) {
                                // Valid access to candidate
                                // Add headers to the response before calling chain.filter()

                                ServerHttpRequest serverHttpRequest= exchange.getRequest().mutate()
                                        .header("LoggedUser", jwtUtil.extractUsername(finalAuthHeader))
                                        .build();
                                return chain.filter(exchange.mutate().request(serverHttpRequest).build());
                            } else {
                                // Invalid access, do not redirect to candidate microservice
                                return Mono.error(new AuthenticationException("Not Authorized"));
                            }
                        })
                        .onErrorResume(WebClientRequestException.class, e -> {
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
