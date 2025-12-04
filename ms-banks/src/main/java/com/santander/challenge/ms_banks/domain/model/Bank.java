package com.santander.challenge.ms_banks.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a bank that can have multiple associated accounts.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bank {
    private UUID id;
    private String code;
    private String name;
    private String country;
    private String address;
    private String phone;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
