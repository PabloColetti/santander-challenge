package com.santander.challenge.ms_banks.application.service;

import com.santander.challenge.ms_banks.domain.model.Bank;
import com.santander.challenge.ms_banks.domain.port.input.BankInternalServicePort;
import com.santander.challenge.ms_banks.domain.port.output.BankInternalClientPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Internal service used to call the microservice itself, showcasing the ability
 * to consume internal endpoints.
 */
@Service
public class BankInternalService implements BankInternalServicePort {
    
    private final BankInternalClientPort bankInternalClient;
    
    public BankInternalService(BankInternalClientPort bankInternalClient) {
        this.bankInternalClient = bankInternalClient;
    }
    
    @Override
    public Bank getBankByIdInternal(UUID id) {
        // Invokes the internal GET /api/banks/{id} endpoint
        return bankInternalClient.getBankById(id);
    }
}

