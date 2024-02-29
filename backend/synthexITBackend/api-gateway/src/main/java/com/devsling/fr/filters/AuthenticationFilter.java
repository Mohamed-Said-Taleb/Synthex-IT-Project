    package com.devsling.fr.filters;

    import com.devsling.fr.exceptions.AuthServiceException;
    import com.devsling.fr.service.AuthApiClient;
    import com.devsling.fr.tools.Constants;
    import org.springframework.cloud.gateway.filter.GatewayFilter;
    import org.springframework.cloud.gateway.filter.GatewayFilterChain;
    import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.server.reactive.ServerHttpRequest;
    import org.springframework.stereotype.Component;
    import org.springframework.web.reactive.function.client.WebClientRequestException;
    import org.springframework.web.server.ServerWebExchange;
    import reactor.core.publisher.Mono;

    import java.util.List;

    import static com.devsling.fr.tools.Constants.MISSING_AUTHORIZATION_HEADER;

    @Component
    public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

        private final RouteValidator validator;
        private final AuthApiClient authWebClient;

        public AuthenticationFilter(RouteValidator validator, AuthApiClient authWebClient) {
            super(Config.class);
            this.validator = validator;
            this.authWebClient = authWebClient;
        }

        @Override
        public GatewayFilter apply(Config config) {
            return ((exchange, chain) -> {
                if (validator.isSecured.test(exchange.getRequest())) {
                    String authHeader = getAuthHeader(exchange.getRequest());
                    if (isAuthMissing(authHeader)) {
                        throw new AuthServiceException(Constants.AUTH_SERVICE_NAME
                                , MISSING_AUTHORIZATION_HEADER, null, HttpStatus.BAD_REQUEST.value());
                    }
                    String finalAuthHeader = extractToken(authHeader);
                    return validateTokenAndFilter(exchange, chain, finalAuthHeader);
                } else {
                    return chain.filter(exchange);
                }
            });
        }

        private String getAuthHeader(ServerHttpRequest request) {
            List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
            return authHeaders != null && !authHeaders.isEmpty() ? authHeaders.get(0) : null;
        }

        private boolean isAuthMissing(String authHeader) {
            return authHeader == null;
        }

        private String extractToken(String authHeader) {
            if (authHeader.startsWith(Constants.BEARER)) {
                return authHeader.substring(Constants.AUTH_HEADER_LENGTH);
            }
            return authHeader;
        }

        private Mono<Void> validateTokenAndFilter(ServerWebExchange exchange, GatewayFilterChain chain, String finalAuthHeader) {
            return authWebClient.validateToken(finalAuthHeader)
                    .flatMap(validateTokenResponse -> {
                        String status = validateTokenResponse.getMessage();

                        if (Constants.VALID_TOKEN.equals(status)) {
                            return chain.filter(exchange);
                        } else if (Constants.INVALID_TOKEN.equals(status)) {
                            return Mono.error(new AuthServiceException(Constants.AUTH_SERVICE_NAME
                                    , Constants.NOT_AUTHORIZED, null, HttpStatus.FORBIDDEN.value()));
                        } else {
                            return Mono.error(new AuthServiceException(Constants.AUTH_SERVICE_NAME,
                                    "Unexpected token validation status: " + status, null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
                        }
                    })
                    .onErrorResume(WebClientRequestException.class, e -> Mono.error(new RuntimeException(Constants.ERROR, e)));
        }


        public static class Config {
        }
    }
