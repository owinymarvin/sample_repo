package com.training.camunda.delegate;

import com.training.camunda.http.HttpProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class HttpServiceDelegate implements JavaDelegate {

    private final HttpProperties httpProperties;
    private final RestClient.Builder restClientBuilder;

    @Override
    public void execute(DelegateExecution execution) {
        String appName = (String) execution.getVariable("appName");
        String rawMethod = (String) execution.getVariable("method");
        String location = (String) execution.getVariable("location");

        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) execution.getVariable("payload");

        HttpMethod httpMethod = httpProperties.validateAndGetHttpMethod(rawMethod);
        String fullUrl = httpProperties.buildFullUrl(appName, location);

        if (payload != null) {
            if (payload.containsKey("docType") && payload.get("docType") != null) {
                payload.put("docType", payload.get("docType").toString().toLowerCase());
            }
            if (payload.containsKey("status") && payload.get("status") != null) {
                payload.put("status", payload.get("status").toString().toUpperCase());
            }
        }

        log.info(
                "Executing HTTP request: app={}, method={}, targetUrl={}, payload={}",
                appName,
                httpMethod,
                fullUrl,
                payload
        );

        RestClient.RequestBodySpec request = restClientBuilder.build()
                .method(httpMethod)
                .uri(fullUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        if (payload != null && !payload.isEmpty() && (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT || httpMethod == HttpMethod.PATCH)) {
            request.body(payload);
        }

        Map<String, Object> result = payload == null
                ? request.retrieve().body(new ParameterizedTypeReference<>() {
        })
                : request.body(payload).retrieve().body(new ParameterizedTypeReference<>() {
        });



        execution.setVariable("result", result);
        System.out.println("Executing HTTP response body: " + result);
    }
}