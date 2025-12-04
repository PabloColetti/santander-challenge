package com.santander.challenge.ms_accounts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) configuration for ms-accounts.
 */
@Configuration
public class OpenApiConfig {
    
    @Value("${server.port:9090}")
    private String serverPort;
    
    @Bean
    public OpenAPI msAccountsOpenAPI() {
        Server directServer = new Server();
        directServer.setUrl("http://localhost:" + serverPort);
        directServer.setDescription("Direct server");
        
        Contact contact = new Contact();
        contact.setName("Santander Challenge");
        contact.setEmail("support@santander-challenge.com");
        
        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");
        
        Info info = new Info()
                .title("MS Accounts API")
                .version("1.0.0")
                .description("REST API for bank account management. This microservice exposes CRUD operations for accounts with per-bank data isolation. Every operation requires the bankId to guarantee isolation and security.")
                .contact(contact)
                .license(license);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(directServer));
    }
}

