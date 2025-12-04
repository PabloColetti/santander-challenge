package com.santander.challenge.ms_accounts.domain.exception;

/**
 * Exception thrown when creating or updating an account with a number that already exists.
 */
public class DuplicateAccountException extends RuntimeException {
    
    public DuplicateAccountException(String accountNumber) {
        super("Account with number " + accountNumber + " already exists");
    }
    
    public DuplicateAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}

