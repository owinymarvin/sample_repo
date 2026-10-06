package com.training.camunda.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CheckRiskLevelDelegate implements JavaDelegate {
    private final Random random = new Random();

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String[] riskLevels = {"LOW", "MEDIUM", "HIGH"};
        String riskLevel = riskLevels[random.nextInt(riskLevels.length)];
        execution.setVariable("riskLevel", "HIGH");
    }
}
