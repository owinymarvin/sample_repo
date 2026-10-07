package com.training.camunda.delegate;

import com.training.camunda.http.ServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component("httpServiceDelegate")
@RequiredArgsConstructor
public class HttpServiceDelegate implements JavaDelegate {

    private final ServiceClient serviceClient;

    @Override
    public void execute(DelegateExecution execution) {

        String appName =
                (String) execution.getVariable("appName");

        String method =
                (String) execution.getVariable("method");

        String location =
                (String) execution.getVariable("location");

        @SuppressWarnings("unchecked")
        Map<String, Object> payload =
                (Map<String, Object>) execution.getVariable("payload");

        log.info(
                "Executing HTTP delegate: app={}, method={}, location={}",
                appName,
                method,
                location
        );

        String result = serviceClient.execute(
                appName,
                method,
                location,
                payload
        );

        execution.setVariable("result", result);

        log.info("HTTP delegate completed: result={}", result);
    }
}