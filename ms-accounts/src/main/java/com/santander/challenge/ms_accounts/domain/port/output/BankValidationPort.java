package com.santander.challenge.ms_accounts.domain.port.output;

import java.util.UUID;

/**
 * Output port that validates bank existence, abstracting communication with ms-banks.
 */
public interface BankValidationPort {
    
    /**
     * Validates whether a bank exists.
     *
     * @param bankId bank identifier
     * @return true when the bank exists, false otherwise
     */
    boolean existsById(UUID bankId);
}

