package com.santander.challenge.ms_banks.domain.port.output;

import com.santander.challenge.ms_banks.domain.model.Bank;

import java.util.UUID;

/**
 * Output port for internal HTTP calls to the microservice, abstracting the Feign client.
 */
public interface BankInternalClientPort {
    
    /**
     * Retrieves a bank via an internal HTTP call.
     *
     * @param id bank identifier
     * @return bank found
     */
    Bank getBankById(UUID id);
}

