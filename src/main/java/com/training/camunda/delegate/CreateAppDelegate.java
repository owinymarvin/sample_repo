package com.training.camunda.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateAppDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
       String drn = generateDrn();
       execution.setVariable("drn", drn);
       execution.setVariable("status", "CREATED");
    }

    private String generateDrn() {
        return "APP-" + UUID.randomUUID().toString().substring(0, 6);
    }
}
