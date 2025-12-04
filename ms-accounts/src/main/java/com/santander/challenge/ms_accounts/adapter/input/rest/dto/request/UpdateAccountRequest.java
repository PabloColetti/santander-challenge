package com.santander.challenge.ms_accounts.adapter.input.rest.dto.request;

import com.santander.challenge.ms_accounts.domain.model.Account;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para la actualización de una cuenta.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {
    
    @NotBlank(message = "Account number is required")
    @Size(max = 20, message = "Account number must not exceed 20 characters")
    private String accountNumber;
    
    @NotBlank(message = "Account holder name is required")
    @Size(max = 100, message = "Account holder name must not exceed 100 characters")
    private String accountHolderName;
    
    @NotNull(message = "Account type is required")
    private Account.AccountType accountType;
    
    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", message = "Balance must be >= 0")
    @Digits(integer = 15, fraction = 2, message = "Balance format is invalid")
    private BigDecimal balance;
    
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters (ISO 4217)")
    private String currency;
    
    private Account.AccountStatus status;
}
