package com.example.clientapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {
    private final Environment environment;

    @Bean(name = "restTemplateServerApi")
    public RestTemplate restTemplateServerApi() {
        return new RestTemplateBuilder()
                .rootUri(environment.getProperty("rest-config.server-api"))
                .build();
    }
}
