    package com.devsling.fr.filters;

    import com.devsling.fr.exceptions.MissingAuthorizationHeaderException;
    import com.devsling.fr.service.AuthApiClient;
    import com.devsling.fr.tools.JwtUtil;
    import org.springframework.cloud.gateway.filter.GatewayFilter;
    import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.server.reactive.ServerHttpRequest;
    import org.springframework.stereotype.Component;
    import org.springframework.web.reactive.function.client.WebClientRequestException;
    import reactor.core.publisher.Mono;

    import javax.naming.AuthenticationException;

    @Component
    public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

        private final RouteValidator validator;
        private final AuthApiClient authWebClient;
        private final JwtUtil jwtUtil;



        public AuthenticationFilter(RouteValidator validator, AuthApiClient authWebClient, JwtUtil jwtUtil) {
            super(Config.class);
            this.validator = validator;
            this.authWebClient = authWebClient;
            this.jwtUtil = jwtUtil;
        }

        @Override
        public GatewayFilter apply(Config config) {
            return ((exchange, chain) -> {
                if (validator.isSecured.test(exchange.getRequest())) {
                    if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                        throw new MissingAuthorizationHeaderException();
                    }
                    String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        authHeader = authHeader.substring(7);
                    }
                    String finalAuthHeader = authHeader;
                    return authWebClient.validateToken(finalAuthHeader)
                            .flatMap(validateTokenResponse -> {
                                String status = validateTokenResponse.getMessage();

                                if ("Valid token".equals(status)) {
                                    return chain.filter(exchange);
                                } else {
                                    return Mono.error(new AuthenticationException("Not Authorized"));
                                }
                            })
                            .onErrorResume(WebClientRequestException.class, e -> Mono.error(new RuntimeException("Error calling auth service", e)));
                }else {
                    return chain.filter(exchange);
                }
            });
        }




        public static class Config {

        }
    }
