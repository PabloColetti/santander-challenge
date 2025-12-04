package com.santander.challenge.ms_banks.application.mapper;

import com.santander.challenge.ms_banks.adapter.input.rest.dto.request.CreateBankRequest;
import com.santander.challenge.ms_banks.adapter.input.rest.dto.request.UpdateBankRequest;
import com.santander.challenge.ms_banks.adapter.input.rest.dto.response.BankResponse;
import com.santander.challenge.ms_banks.adapter.output.persistence.entity.BankEntity;
import com.santander.challenge.ms_banks.domain.model.Bank;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre diferentes representaciones de Bank.
 * Implementa el patrón Factory Method para crear DTOs.
 */
@Component
public class BankMapper {
    
    /**
     * Convierte un Bank (dominio) a BankResponse (DTO).
     */
    public BankResponse toResponse(Bank bank) {
        if (bank == null) {
            return null;
        }
        
        BankResponse response = new BankResponse();
        response.setId(bank.getId());
        response.setCode(bank.getCode());
        response.setName(bank.getName());
        response.setCountry(bank.getCountry());
        response.setAddress(bank.getAddress());
        response.setPhone(bank.getPhone());
        response.setEmail(bank.getEmail());
        response.setCreatedAt(bank.getCreatedAt());
        response.setUpdatedAt(bank.getUpdatedAt());
        return response;
    }
    
    /**
     * Convierte un CreateBankRequest (DTO) a Bank (dominio).
     */
    public Bank toDomain(CreateBankRequest request) {
        if (request == null) {
            return null;
        }
        
        Bank bank = new Bank();
        bank.setCode(request.getCode());
        bank.setName(request.getName());
        bank.setCountry(request.getCountry());
        bank.setAddress(request.getAddress());
        bank.setPhone(request.getPhone());
        bank.setEmail(request.getEmail());
        return bank;
    }
    
    /**
     * Convierte un UpdateBankRequest (DTO) a Bank (dominio).
     */
    public Bank toDomain(UpdateBankRequest request) {
        if (request == null) {
            return null;
        }
        
        Bank bank = new Bank();
        bank.setCode(request.getCode());
        bank.setName(request.getName());
        bank.setCountry(request.getCountry());
        bank.setAddress(request.getAddress());
        bank.setPhone(request.getPhone());
        bank.setEmail(request.getEmail());
        return bank;
    }
    
    /**
     * Convierte un BankEntity (persistencia) a Bank (dominio).
     */
    public Bank toDomain(BankEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Bank bank = new Bank();
        bank.setId(entity.getId());
        bank.setCode(entity.getCode());
        bank.setName(entity.getName());
        bank.setCountry(entity.getCountry());
        bank.setAddress(entity.getAddress());
        bank.setPhone(entity.getPhone());
        bank.setEmail(entity.getEmail());
        bank.setCreatedAt(entity.getCreatedAt());
        bank.setUpdatedAt(entity.getUpdatedAt());
        return bank;
    }
    
    /**
     * Convierte un Bank (dominio) a BankEntity (persistencia).
     */
    public BankEntity toEntity(Bank bank) {
        if (bank == null) {
            return null;
        }
        
        BankEntity entity = new BankEntity();
        entity.setId(bank.getId());
        entity.setCode(bank.getCode());
        entity.setName(bank.getName());
        entity.setCountry(bank.getCountry());
        entity.setAddress(bank.getAddress());
        entity.setPhone(bank.getPhone());
        entity.setEmail(bank.getEmail());
        entity.setCreatedAt(bank.getCreatedAt());
        entity.setUpdatedAt(bank.getUpdatedAt());
        return entity;
    }
}

