package com.santander.challenge.ms_accounts.adapter.output.persistence.entity;

import com.santander.challenge.ms_accounts.domain.model.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity for Account.
 */
@Entity
@Table(name = "accounts", uniqueConstraints = {
    @UniqueConstraint(columnNames = "account_number")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;
    
    @Column(name = "bank_id", nullable = false)
    private UUID bankId;
    
    @Column(name = "account_holder_name", nullable = false, length = 100)
    private String accountHolderName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private Account.AccountType accountType;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;
    
    @Column(nullable = false, length = 3)
    private String currency;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Account.AccountStatus status;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
