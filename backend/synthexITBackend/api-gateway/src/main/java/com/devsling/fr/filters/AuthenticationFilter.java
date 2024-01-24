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
                    throw new AuthException(HttpStatus.UNAUTHORIZED.value(), "Authorization Header Missing", AuthErrorMessage.builder()
                            .error("Authorization Header Missing")
                            .build());
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    // Validate the token using your JWTUtil or authentication service
                    jwtUtil.validateToken(authHeader);

                } catch (AuthException e) {
                    // You can rethrow AuthException if needed, or handle it appropriately
                    throw e;

                } catch (Exception e) {
                    // Handle other exceptions, or rethrow them as AuthException
                    throw new AuthException(HttpStatus.UNAUTHORIZED.value(), "Invalid Access", AuthErrorMessage.builder()
                            .error("Invalid Access")
                            .build());
                }
            }
            return chain.filter(exchange);
        });
    }



    public static class Config {

    }
}
