package com.santander.challenge.ms_accounts.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that enables Feign clients for the ms-accounts module.
 */
@Configuration
@EnableFeignClients(basePackages = "com.santander.challenge.ms_accounts.adapter")
public class FeignConfig {
    // Additional Feign configuration can be added here if required.
}

