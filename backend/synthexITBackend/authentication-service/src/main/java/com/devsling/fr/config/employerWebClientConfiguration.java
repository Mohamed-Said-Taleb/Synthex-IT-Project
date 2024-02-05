package com.devsling.fr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Configuration
@ConfigurationProperties(prefix = "backend.employer")
public class employerWebClientConfiguration extends WebClientConfiguration {
    @Bean
    public WebClient employerWebClient(WebClient.Builder webClientBuilder) {
        return super.webClientBuilder("employer", webClientBuilder, Collections.emptyList()).build();
    }
}
