package com.santander.challenge.ms_banks.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para Feign Clients.
 */
@Configuration
@EnableFeignClients(basePackages = "com.santander.challenge.ms_banks.adapter")
public class FeignConfig {
    // Configuración adicional de Feign si es necesaria
}

