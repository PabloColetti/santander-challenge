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
 * Feign client used to communicate with ms-banks and validate bank existence.
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
 * Adapter that implements BankValidationPort using Feign.
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
            // If the bank does not exist (404), return false
            log.debug("Bank with id {} not found in ms-banks (404)", bankId);
            return false;
        } catch (FeignException e) {
            // For other HTTP errors log the issue and return false
            log.error("Feign error calling ms-banks for bankId {}: Status={}, Message={}", 
                    bankId, e.status(), e.getMessage());
            return false;
        } catch (Exception e) {
            // If ms-banks is unavailable or another error occurs, log and return false
            log.error("Unexpected error calling ms-banks for bankId {}: {}", bankId, e.getMessage(), e);
            return false;
        }
    }
}

