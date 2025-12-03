package com.santander.challenge.ms_config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ConfigServerController {

    @GetMapping("/")
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "Config Server");
        info.put("status", "running");
        info.put("port", 8888);
        info.put("endpoints", Map.of(
            "format", "/{application}/{profile}",
            "examples", Map.of(
                "ms-eureka", "/ms-eureka/default",
                "ms-banks", "/ms-banks/default",
                "api-consumer", "/api-consumer/default",
                "ms-accounts", "/ms-accounts/default"
            )
        ));
        return info;
    }
}

