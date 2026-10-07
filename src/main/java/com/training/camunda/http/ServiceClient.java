package com.training.camunda.http;

import java.util.Map;

public interface ServiceClient {

    String execute(String appName, String method, String location,
                   Map<String, Object> payload);
}