package com.devsling.fr.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuring the gateway to redirect Actuator endpoint requests to the corresponding microservice.
 *
 * @Author [Mohamed Said Taleb]
 */
@Configuration
public class ActuatorConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authentication-service", r -> r.path("/actuator/**")
                        .filters(f -> f.rewritePath("/actuator/(?<segment>.*)",
                                "/${segment}")).uri("lb://AUTHENTICATION-SERVICE"))
                .route("candidate-service", r -> r.path("/actuator/**")
                        .filters(f -> f.rewritePath("/actuator/(?<segment>.*)",
                                "/${segment}")).uri("lb://CANDIDATE-SERVICE"))
                .route("employer-service", r -> r.path("/actuator/**")
                        .filters(f -> f.rewritePath("/actuator/(?<segment>.*)",
                                "/${segment}")).uri("lb://EMPLOYER-SERVICE"))
                .build();
    }
}
