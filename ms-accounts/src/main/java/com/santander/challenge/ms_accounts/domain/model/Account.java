package com.santander.challenge.ms_accounts.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a bank account that belongs to a specific bank.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private UUID id;
    private String accountNumber;
    private UUID bankId;
    private String accountHolderName;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Supported account types.
     */
    public enum AccountType {
        CHECKING,
        SAVINGS,
        BUSINESS
    }
    
    /**
     * Supported account statuses.
     */
    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        BLOCKED
    }
}
