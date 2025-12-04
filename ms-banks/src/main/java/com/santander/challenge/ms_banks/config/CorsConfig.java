package com.santander.challenge.ms_banks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuración de CORS para permitir peticiones desde Swagger UI y el API Gateway.
 */
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir todos los orígenes (en producción, especificar orígenes concretos)
        config.addAllowedOriginPattern("*");
        
        config.addAllowedMethod("*");
        
        config.addAllowedHeader("*");
        
        // Permitir credenciales (false cuando usamos allowedOriginPattern("*"))
        config.setAllowCredentials(false);
        
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}

