package com.training.camunda.delegate;

import com.training.camunda.config.HttpConfigProperties;
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
public class UpdateRejectedDocumentDelegate implements JavaDelegate {

    private final HttpConfigProperties createDocumentConfigProperties;
    private final RestClient.Builder restClientBuilder;

    @Override
    public void execute(DelegateExecution execution) {
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) execution.getVariable("result");

        if (result == null || !result.containsKey("id") || result.get("id") == null) {
            throw new IllegalArgumentException("Process variable 'result' must contain a valid document 'id'.");
        }

        String documentId = result.get("id").toString();
        String appName = "document";
        String fullUrl = createDocumentConfigProperties.buildFullUrl(appName, "document/status");

        Map<String, Object> updatePayload = new HashMap<>();
        updatePayload.put("id", documentId);
        updatePayload.put("status", "REJECTED");

        log.info("Updating document status to REJECTED: targetUrl={}, payload={}", fullUrl, updatePayload);

        Map<String, Object> updatedResult = restClientBuilder.build()
                .method(HttpMethod.PATCH)
                .uri(fullUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(updatePayload)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        execution.setVariable("result", updatedResult);
        log.info("Document status update response: {}", updatedResult);
    }
}