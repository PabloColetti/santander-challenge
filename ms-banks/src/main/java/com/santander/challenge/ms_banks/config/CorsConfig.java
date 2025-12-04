package com.santander.challenge.ms_banks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS configuration that allows requests from Swagger UI and the API Gateway.
 */
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.addAllowedOriginPattern("*");
        
        config.addAllowedMethod("*");
        
        config.addAllowedHeader("*");
        
        config.setAllowCredentials(false);
        
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}

