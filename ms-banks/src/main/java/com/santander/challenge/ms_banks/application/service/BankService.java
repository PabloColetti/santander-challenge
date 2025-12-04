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
 * Servicio que implementa los casos de uso de Bank.
 * Contiene la lógica de negocio.
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
        // Validar duplicidad de código
        if (bankRepository.existsByCode(bank.getCode())) {
            throw new DuplicateBankException(bank.getCode());
        }
        
        // Normalizar código a mayúsculas
        bank.setCode(bank.getCode().toUpperCase());
        
        // Establecer timestamps
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
        // Verificar que el banco existe
        Bank existingBank = bankRepository.findById(id)
                .orElseThrow(() -> new BankNotFoundException(id));
        
        // Validar duplicidad de código si cambió
        if (!existingBank.getCode().equals(bank.getCode())) {
            if (bankRepository.existsByCodeAndIdNot(bank.getCode(), id)) {
                throw new DuplicateBankException(bank.getCode());
            }
            existingBank.setCode(bank.getCode().toUpperCase());
        }
        
        // Actualizar campos
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
        // Verificar que el banco existe
        bankRepository.findById(id)
                .orElseThrow(() -> new BankNotFoundException(id));
        
        // Verificar que no tenga cuentas asociadas
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

