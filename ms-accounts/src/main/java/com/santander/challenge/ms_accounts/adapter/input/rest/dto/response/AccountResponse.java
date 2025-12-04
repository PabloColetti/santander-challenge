package com.santander.challenge.ms_accounts.adapter.input.rest.dto.response;

import com.santander.challenge.ms_accounts.domain.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de respuesta para una cuenta.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    
    private UUID id;
    private String accountNumber;
    private UUID bankId;
    private String accountHolderName;
    private Account.AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private Account.AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
