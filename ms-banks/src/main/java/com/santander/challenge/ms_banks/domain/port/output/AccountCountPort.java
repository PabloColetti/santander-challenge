package com.santander.challenge.ms_banks.domain.port.output;

import java.util.UUID;

/**
 * Output port to count accounts belonging to a bank, abstracting communication with ms-accounts.
 */
public interface AccountCountPort {
    
    /**
     * Counts accounts associated with a bank.
     *
     * @param bankId bank identifier
     * @return number of accounts
     */
    Long countByBankId(UUID bankId);
}

