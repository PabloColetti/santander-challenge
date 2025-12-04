package com.santander.challenge.ms_banks.adapter.input.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO representing a bank.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankResponse {
    
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
