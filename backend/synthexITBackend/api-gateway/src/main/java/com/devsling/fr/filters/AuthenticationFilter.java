package com.devsling.fr.filters;


import com.devsling.fr.exceptions.AuthErrorMessage;
import com.devsling.fr.exceptions.AuthException;
import com.devsling.fr.tools.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

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

                try {
                    // REST call to AUTH service with WebClient
                    String finalAuthHeader = authHeader;
                 /*  String response = authWebClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/auth/validate")
                                    .queryParam("token", finalAuthHeader)
                                    .build())
                            .retrieve()
                            .bodyToMono(String.class)
                            .block(); // Blocking call to get the response*/
                    jwtUtil.validateToken(authHeader);


                } catch (AuthException e) {
                    // You can rethrow AuthException if needed, or handle it appropriately
                    throw e;

                } catch (Exception e) {
                    // Handle other exceptions, or rethrow them as AuthException
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }



    public static class Config {

    }
}
