package com.santander.challenge.ms_banks.application.service;

import com.santander.challenge.ms_banks.domain.model.Bank;
import com.santander.challenge.ms_banks.domain.port.input.BankInternalServicePort;
import com.santander.challenge.ms_banks.domain.port.output.BankInternalClientPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Servicio interno que permite llamadas al propio microservicio.
 * Demuestra la capacidad de consumir endpoints internos.
 */
@Service
public class BankInternalService implements BankInternalServicePort {
    
    private final BankInternalClientPort bankInternalClient;
    
    public BankInternalService(BankInternalClientPort bankInternalClient) {
        this.bankInternalClient = bankInternalClient;
    }
    
    @Override
    public Bank getBankByIdInternal(UUID id) {
        // Llama internamente al endpoint GET /api/banks/{id}
        return bankInternalClient.getBankById(id);
    }
}

