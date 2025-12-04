package com.santander.challenge.ms_accounts.application.mapper;

import com.santander.challenge.ms_accounts.adapter.input.rest.dto.request.CreateAccountRequest;
import com.santander.challenge.ms_accounts.adapter.input.rest.dto.request.UpdateAccountRequest;
import com.santander.challenge.ms_accounts.adapter.input.rest.dto.response.AccountResponse;
import com.santander.challenge.ms_accounts.adapter.output.persistence.entity.AccountEntity;
import com.santander.challenge.ms_accounts.domain.model.Account;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre diferentes representaciones de Account.
 */
@Component
public class AccountMapper {
    
    public AccountResponse toResponse(Account account) {
        if (account == null) {
            return null;
        }
        
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setBankId(account.getBankId());
        response.setAccountHolderName(account.getAccountHolderName());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        response.setCurrency(account.getCurrency());
        response.setStatus(account.getStatus());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
    
    public Account toDomain(CreateAccountRequest request) {
        if (request == null) {
            return null;
        }
        
        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setBankId(request.getBankId());
        account.setAccountHolderName(request.getAccountHolderName());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());
        account.setCurrency(request.getCurrency());
        account.setStatus(request.getStatus());
        return account;
    }
    
    public Account toDomain(UpdateAccountRequest request) {
        if (request == null) {
            return null;
        }
        
        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setAccountHolderName(request.getAccountHolderName());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());
        account.setCurrency(request.getCurrency());
        account.setStatus(request.getStatus());
        return account;
    }
    
    public Account toDomain(AccountEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Account account = new Account();
        account.setId(entity.getId());
        account.setAccountNumber(entity.getAccountNumber());
        account.setBankId(entity.getBankId());
        account.setAccountHolderName(entity.getAccountHolderName());
        account.setAccountType(entity.getAccountType());
        account.setBalance(entity.getBalance());
        account.setCurrency(entity.getCurrency());
        account.setStatus(entity.getStatus());
        account.setCreatedAt(entity.getCreatedAt());
        account.setUpdatedAt(entity.getUpdatedAt());
        return account;
    }
    
    public AccountEntity toEntity(Account account) {
        if (account == null) {
            return null;
        }
        
        AccountEntity entity = new AccountEntity();
        entity.setId(account.getId());
        entity.setAccountNumber(account.getAccountNumber());
        entity.setBankId(account.getBankId());
        entity.setAccountHolderName(account.getAccountHolderName());
        entity.setAccountType(account.getAccountType());
        entity.setBalance(account.getBalance());
        entity.setCurrency(account.getCurrency());
        entity.setStatus(account.getStatus());
        entity.setCreatedAt(account.getCreatedAt());
        entity.setUpdatedAt(account.getUpdatedAt());
        return entity;
    }
}

