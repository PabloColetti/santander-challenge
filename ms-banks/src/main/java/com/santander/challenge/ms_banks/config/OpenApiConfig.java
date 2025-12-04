package com.santander.challenge.ms_banks.config;

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
 * Configuración de OpenAPI (Swagger) para ms-banks.
 */
@Configuration
public class OpenApiConfig {
    
    @Value("${server.port:8090}")
    private String serverPort;
    
    @Bean
    public OpenAPI msBanksOpenAPI() {
        Server directServer = new Server();
        directServer.setUrl("http://localhost:" + serverPort);
        directServer.setDescription("Servidor directo");
        
        Contact contact = new Contact();
        contact.setName("Santander Challenge");
        contact.setEmail("support@santander-challenge.com");
        
        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");
        
        Info info = new Info()
                .title("MS Banks API")
                .version("1.0.0")
                .description("API REST para la gestión de bancos. " +
                        "Este microservicio permite realizar operaciones CRUD sobre bancos, " +
                        "incluyendo creación, consulta, actualización y eliminación.")
                .contact(contact)
                .license(license);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(directServer));
    }
}

