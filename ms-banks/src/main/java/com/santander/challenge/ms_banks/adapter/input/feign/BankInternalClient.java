package com.santander.challenge.ms_banks.adapter.input.feign;

import com.santander.challenge.ms_banks.adapter.input.rest.dto.response.BankResponse;
import com.santander.challenge.ms_banks.domain.model.Bank;
import com.santander.challenge.ms_banks.domain.port.output.BankInternalClientPort;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * Feign client for internal calls, enabling the microservice to consume its own endpoints.
 */
@FeignClient(name = "ms-banks", url = "${feign.client.ms-banks.url:http://localhost:8090}")
interface BankInternalFeignClient {
    
    @GetMapping("/api/banks/{id}")
    BankResponse getBankById(@PathVariable UUID id);
}

/**
 * Adapter that implements BankInternalClientPort using Feign.
 */
@Component
class BankInternalClientAdapter implements BankInternalClientPort {
    
    private final BankInternalFeignClient feignClient;
    
    public BankInternalClientAdapter(BankInternalFeignClient feignClient) {
        this.feignClient = feignClient;
    }
    
    @Override
    public Bank getBankById(UUID id) {
        BankResponse response = feignClient.getBankById(id);
        // Map the response DTO to the domain model
        Bank bank = new Bank();
        bank.setId(response.getId());
        bank.setCode(response.getCode());
        bank.setName(response.getName());
        bank.setCountry(response.getCountry());
        bank.setAddress(response.getAddress());
        bank.setPhone(response.getPhone());
        bank.setEmail(response.getEmail());
        bank.setCreatedAt(response.getCreatedAt());
        bank.setUpdatedAt(response.getUpdatedAt());
        return bank;
    }
}

