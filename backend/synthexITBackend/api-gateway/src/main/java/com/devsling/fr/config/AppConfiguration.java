package com.devsling.fr.config;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.Clock;
import java.util.HashMap;
@Configuration
public class AppConfiguration {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public InfoContributor appInfoContributor(Environment environment) {
        return info -> info.withDetail("app", new HashMap() {{
            put("name", environment.getProperty("info.app.name"));
            put("description", environment.getProperty("info.app.description"));
            put("version", environment.getProperty("info.app.version"));
        }});
    }
}
