package com.training.camunda.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "http")
@Validated
public class HttpConfigProperties {

    @NotNull(message = "HTTP applications map must not be null")
    private Map<String, ApplicationConfig> applications;

    public String getUrlForApp(String appName) {
        if (appName == null || appName.isBlank()) {
            throw new IllegalArgumentException("Application name must not be blank.");
        }
        if (applications == null || !applications.containsKey(appName)) {
            throw new IllegalArgumentException(
                    String.format("HTTP application config for '%s' is missing from configuration properties.", appName)
            );
        }
        return applications.get(appName).getUrl();
    }

    public String buildFullUrl(String appName, String location) {
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location/endpoint path must not be blank.");
        }

        String baseUrl = getUrlForApp(appName).trim();
        String path = location.trim();

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        return baseUrl + path;
    }

    public HttpMethod validateAndGetHttpMethod(String method) {
        if (!(method == null)){
            try {
                return HttpMethod.valueOf(method.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        String.format("Invalid HTTP method '%s'. Supported methods: GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD.", method),
                        e
                );
            }
        }
        return HttpMethod.valueOf("POST");
    }

    @Data
    public static class ApplicationConfig {
        @NotBlank(message = "URL must not be blank")
        private String url;
    }
}