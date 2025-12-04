package com.santander.challenge.ms_banks.domain.port.input;

import com.santander.challenge.ms_banks.domain.model.Bank;

import java.util.UUID;

/**
 * Input port for the internal Bank service that performs self-calls within the microservice.
 */
public interface BankInternalServicePort {
    
    /**
     * Retrieves a bank via an internal call (self-consumption).
     * Demonstrates the microservice's ability to call its own endpoints.
     *
     * @param id bank identifier
     * @return bank found
     * @throws com.santander.challenge.ms_banks.domain.exception.BankNotFoundException when the bank does not exist
     */
    Bank getBankByIdInternal(UUID id);
}

