package com.santander.challenge.ms_banks.domain.exception;

import java.util.UUID;

/**
 * Exception thrown when attempting to delete a bank that still has associated accounts.
 */
public class BankHasAccountsException extends RuntimeException {
    
    public BankHasAccountsException(UUID bankId, Long accountCount) {
        super("Bank with id " + bankId + " cannot be deleted because it has " + accountCount + " associated accounts");
    }
    
    public BankHasAccountsException(String message) {
        super(message);
    }
}

