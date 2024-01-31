package com.devsling.fr.adapters.backend.config;

import com.devsling.fr.config.WebClientConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Configuration
@ConfigurationProperties(prefix = "backend.candidate")
public class CandidateWebClientConfig extends WebClientConfiguration {
    @Bean
    public WebClient candidateWebClient(WebClient.Builder webClientBuilder) {
        return super.webClientBuilder("candidate", webClientBuilder, Collections.emptyList()).build();
    }
}
