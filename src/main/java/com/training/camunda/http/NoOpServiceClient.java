package com.training.camunda.http;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component
@ConditionalOnProperty(
        prefix = "http",
        name = "send-type",
        havingValue = "none"
)
public class NoOpServiceClient implements ServiceClient {

    @Override
    public String execute(
            String appName,
            String method,
            String location,
            Map<String, Object> payload
    ) {

        log.debug(
                "HTTP service call disabled: app={}, method={}, location={}",
                appName,
                method,
                location
        );

        return null;
    }
}