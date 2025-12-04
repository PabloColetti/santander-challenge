package com.santander.challenge.ms_accounts.domain.exception;

/**
 * Exception thrown when a bank attempts to access accounts that do not belong to it.
 */
public class UnauthorizedAccessException extends RuntimeException {
    
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

