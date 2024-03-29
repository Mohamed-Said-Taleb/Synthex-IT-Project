package com.devsling.fr.backend;

import com.devsling.fr.config.WebClientConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Configuration
@ConfigurationProperties(prefix = "backend.auth")
public class AuthWebClientConfiguration extends WebClientConfiguration {
    @Bean
    public WebClient authWebClient(WebClient.Builder webClientBuilder) {
        return super.webClientBuilder("auth", webClientBuilder, Collections.emptyList()).build();
    }
}
