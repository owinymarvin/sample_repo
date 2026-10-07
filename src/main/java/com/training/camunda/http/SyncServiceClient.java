package com.training.camunda.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "http",
        name = "send-type",
        havingValue = "sync",
        matchIfMissing = true
)
public class SyncServiceClient implements ServiceClient {

    private final RestClient.Builder restClientBuilder;
    private final HttpApplicationProperties properties;

    @Override
    public String execute(
            String appName,
            String method,
            String location,
            Map<String, Object> payload
    ) {

        HttpApplicationProperties.Application application =
                properties.getApplications().get(appName);

        if (application == null) {
            throw new IllegalArgumentException(
                    "Unknown HTTP application: " + appName
            );
        }

        String url = application.getUrl() + location;

        log.info(
                "Calling HTTP service: app={}, method={}, url={}",
                appName,
                method,
                url
        );

        return restClientBuilder
                .build()
                .method(HttpMethod.valueOf(method.toUpperCase()))
                .uri(url)
                .body(payload)
                .retrieve()
                .body(String.class);
    }
}