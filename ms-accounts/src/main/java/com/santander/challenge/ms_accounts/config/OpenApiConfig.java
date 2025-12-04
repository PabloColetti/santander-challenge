package com.santander.challenge.ms_accounts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI (Swagger) para ms-accounts.
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI msAccountsOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:9090");
        localServer.setDescription("Servidor local");
        
        Server dockerServer = new Server();
        dockerServer.setUrl("http://ms-accounts:9090");
        dockerServer.setDescription("Servidor Docker");
        
        Server gatewayServer = new Server();
        gatewayServer.setUrl("http://localhost:8080");
        gatewayServer.setDescription("API Gateway");
        
        Contact contact = new Contact();
        contact.setName("Santander Challenge");
        contact.setEmail("support@santander-challenge.com");
        
        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");
        
        Info info = new Info()
                .title("MS Accounts API")
                .version("1.0.0")
                .description("API REST para la gestión de cuentas bancarias. " +
                        "Este microservicio permite realizar operaciones CRUD sobre cuentas, " +
                        "con aislamiento de datos por banco. Todas las operaciones requieren " +
                        "el bankId para garantizar la seguridad y el aislamiento de datos.")
                .contact(contact)
                .license(license);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(gatewayServer, localServer, dockerServer));
    }
}

