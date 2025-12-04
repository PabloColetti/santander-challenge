package com.santander.challenge.ms_accounts.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad de dominio Account.
 * Representa una cuenta bancaria que pertenece a un banco específico.
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
     * Enum para tipos de cuenta.
     */
    public enum AccountType {
        CHECKING,
        SAVINGS,
        BUSINESS
    }
    
    /**
     * Enum para estados de cuenta.
     */
    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        BLOCKED
    }
}
