package com.training.camunda.http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "http")
public class HttpApplicationProperties {

    private Map<String, Application> applications = new HashMap<>();

    @Getter
    @Setter
    public static class Application {
        private String url;
    }
}