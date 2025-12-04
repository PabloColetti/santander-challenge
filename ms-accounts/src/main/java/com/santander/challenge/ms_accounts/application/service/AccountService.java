package com.santander.challenge.ms_accounts.application.service;

import com.santander.challenge.ms_accounts.domain.exception.AccountNotFoundException;
import com.santander.challenge.ms_accounts.domain.exception.BankNotFoundException;
import com.santander.challenge.ms_accounts.domain.exception.DuplicateAccountException;
import com.santander.challenge.ms_accounts.domain.exception.UnauthorizedAccessException;
import com.santander.challenge.ms_accounts.domain.model.Account;
import com.santander.challenge.ms_accounts.domain.port.input.AccountServicePort;
import com.santander.challenge.ms_accounts.domain.port.output.AccountRepositoryPort;
import com.santander.challenge.ms_accounts.domain.port.output.BankValidationPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service that implements the Account use cases and enforces business logic plus isolation rules.
 */
@Service
@Transactional
public class AccountService implements AccountServicePort {
    
    private final AccountRepositoryPort accountRepository;
    private final BankValidationPort bankValidationPort;
    
    public AccountService(AccountRepositoryPort accountRepository, 
                          BankValidationPort bankValidationPort) {
        this.accountRepository = accountRepository;
        this.bankValidationPort = bankValidationPort;
    }
    
    @Override
    public Account createAccount(Account account) {
        // Validate the bank exists
        if (!bankValidationPort.existsById(account.getBankId())) {
            throw new BankNotFoundException(account.getBankId());
        }
        
        // Ensure the account number is unique
        if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            throw new DuplicateAccountException(account.getAccountNumber());
        }
        
        // Validate initial balance >= 0
        if (account.getBalance() == null || account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new com.santander.challenge.ms_accounts.domain.exception.AccountValidationException(
                    "Initial balance must be >= 0");
        }
        
        // Assign timestamps
        LocalDateTime now = LocalDateTime.now();
        account.setCreatedAt(now);
        account.setUpdatedAt(now);
        
        // Set default status if none is provided
        if (account.getStatus() == null) {
            account.setStatus(Account.AccountStatus.ACTIVE);
        }
        
        return accountRepository.save(account);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Account getAccountById(UUID id, UUID bankId) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        
        // Isolation validation: the account must belong to the provided bank
        if (!account.getBankId().equals(bankId)) {
            throw new UnauthorizedAccessException(
                    "Account with id " + id + " does not belong to bank " + bankId);
        }
        
        return account;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Account> getAccountsByBankId(UUID bankId, Pageable pageable) {
        // Isolation guarantee: only returns accounts for the specified bankId
        return accountRepository.findByBankId(bankId, pageable);
    }
    
    @Override
    public Account updateAccount(UUID id, UUID bankId, Account account) {
        // Ensure the account exists and belongs to the bank
        Account existingAccount = getAccountById(id, bankId);
        
        // Ensure the account number remains unique when changed
        if (!existingAccount.getAccountNumber().equals(account.getAccountNumber())) {
            if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
                throw new DuplicateAccountException(account.getAccountNumber());
            }
            existingAccount.setAccountNumber(account.getAccountNumber());
        }
        
        // Update mutable fields (bankId cannot change)
        existingAccount.setAccountHolderName(account.getAccountHolderName());
        existingAccount.setAccountType(account.getAccountType());
        existingAccount.setBalance(account.getBalance());
        existingAccount.setCurrency(account.getCurrency());
        existingAccount.setStatus(account.getStatus());
        existingAccount.setUpdatedAt(LocalDateTime.now());
        
        return accountRepository.save(existingAccount);
    }
    
    @Override
    public void deleteAccount(UUID id, UUID bankId) {
        // Ensure the account exists and belongs to the bank
        getAccountById(id, bankId);
        
        accountRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long countByBankId(UUID bankId) {
        return accountRepository.countByBankId(bankId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }
}

