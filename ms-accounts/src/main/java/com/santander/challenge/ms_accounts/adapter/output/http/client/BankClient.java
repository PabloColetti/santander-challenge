package com.santander.challenge.ms_accounts.adapter.output.http.client;

import com.santander.challenge.ms_accounts.domain.port.output.BankValidationPort;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * Cliente Feign para comunicarse con ms-banks.
 * Permite validar que un banco existe.
 */
@FeignClient(name = "ms-banks", url = "${feign.client.ms-banks.url:http://localhost:8090}")
interface BankFeignClient {
    
    @GetMapping("/api/banks/{id}")
    BankResponse getBankById(@PathVariable UUID id);
    
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    class BankResponse {
        private UUID id;
        private String code;
        private String name;
        private String country;
        private String address;
        private String phone;
        private String email;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;
    }
}

/**
 * Adaptador que implementa BankValidationPort usando Feign.
 */
@Component
class BankValidationAdapter implements BankValidationPort {
    
    private static final Logger log = LoggerFactory.getLogger(BankValidationAdapter.class);
    private final BankFeignClient bankFeignClient;
    
    public BankValidationAdapter(BankFeignClient bankFeignClient) {
        this.bankFeignClient = bankFeignClient;
    }
    
    @Override
    public boolean existsById(UUID bankId) {
        try {
            BankFeignClient.BankResponse response = bankFeignClient.getBankById(bankId);
            boolean exists = response != null && response.getId() != null;
            log.debug("Bank validation for id {}: {}", bankId, exists);
            return exists;
        } catch (FeignException.NotFound e) {
            // Si el banco no existe (404), retornar false
            log.debug("Bank with id {} not found in ms-banks (404)", bankId);
            return false;
        } catch (FeignException e) {
            // Para otros errores HTTP de Feign, loguear y retornar false
            log.error("Feign error calling ms-banks for bankId {}: Status={}, Message={}", 
                    bankId, e.status(), e.getMessage());
            return false;
        } catch (Exception e) {
            // Si ms-banks no está disponible o hay otro error, loguear y retornar false
            log.error("Unexpected error calling ms-banks for bankId {}: {}", bankId, e.getMessage(), e);
            return false;
        }
    }
}

