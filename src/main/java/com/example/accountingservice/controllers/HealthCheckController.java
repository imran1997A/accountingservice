package com.example.accountingservice.controllers;

import com.example.accountingservice.constant.ServiceUris;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping(value = ServiceUris.HEALTH_CHECK_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public String healthCheck() {
        return "Hello from accounting-service! I am up and running!!";
    }
}
