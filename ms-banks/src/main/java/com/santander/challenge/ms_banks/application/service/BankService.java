package com.santander.challenge.ms_banks.application.service;

import com.santander.challenge.ms_banks.domain.exception.BankHasAccountsException;
import com.santander.challenge.ms_banks.domain.exception.BankNotFoundException;
import com.santander.challenge.ms_banks.domain.exception.DuplicateBankException;
import com.santander.challenge.ms_banks.domain.model.Bank;
import com.santander.challenge.ms_banks.domain.port.input.BankServicePort;
import com.santander.challenge.ms_banks.domain.port.output.AccountCountPort;
import com.santander.challenge.ms_banks.domain.port.output.BankRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service that implements the Bank use cases and encapsulates the domain logic.
 */
@Service
@Transactional
public class BankService implements BankServicePort {
    
    private final BankRepositoryPort bankRepository;
    private final AccountCountPort accountCountPort;
    
    public BankService(BankRepositoryPort bankRepository, AccountCountPort accountCountPort) {
        this.bankRepository = bankRepository;
        this.accountCountPort = accountCountPort;
    }
    
    @Override
    public Bank createBank(Bank bank) {
        // Validate unique code constraint
        if (bankRepository.existsByCode(bank.getCode())) {
            throw new DuplicateBankException(bank.getCode());
        }
        
        // Normalize the code to uppercase
        bank.setCode(bank.getCode().toUpperCase());
        
        // Assign timestamps
        LocalDateTime now = LocalDateTime.now();
        bank.setCreatedAt(now);
        bank.setUpdatedAt(now);
        
        return bankRepository.save(bank);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Bank getBankById(UUID id) {
        return bankRepository.findById(id)
                .orElseThrow(() -> new BankNotFoundException(id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Bank> getAllBanks(Pageable pageable) {
        return bankRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Bank> getBanksByCountry(String country, Pageable pageable) {
        return bankRepository.findByCountry(country, pageable);
    }
    
    @Override
    public Bank updateBank(UUID id, Bank bank) {
        // Ensure the bank exists
        Bank existingBank = bankRepository.findById(id)
                .orElseThrow(() -> new BankNotFoundException(id));
        
        // Validate code uniqueness when it changes
        if (!existingBank.getCode().equals(bank.getCode())) {
            if (bankRepository.existsByCodeAndIdNot(bank.getCode(), id)) {
                throw new DuplicateBankException(bank.getCode());
            }
            existingBank.setCode(bank.getCode().toUpperCase());
        }
        
        // Update mutable fields
        existingBank.setName(bank.getName());
        existingBank.setCountry(bank.getCountry());
        existingBank.setAddress(bank.getAddress());
        existingBank.setPhone(bank.getPhone());
        existingBank.setEmail(bank.getEmail());
        existingBank.setUpdatedAt(LocalDateTime.now());
        
        return bankRepository.save(existingBank);
    }
    
    @Override
    public void deleteBank(UUID id) {
        // Ensure the bank exists
        bankRepository.findById(id)
                .orElseThrow(() -> new BankNotFoundException(id));
        
        // Ensure there are no associated accounts
        Long accountCount = accountCountPort.countByBankId(id);
        if (accountCount > 0) {
            throw new BankHasAccountsException(id, accountCount);
        }
        
        bankRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return bankRepository.existsByCode(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Bank> findByCode(String code) {
        return bankRepository.findByCode(code);
    }
}

