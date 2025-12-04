package com.santander.challenge.ms_banks.adapter.output.persistence.adapter;

import com.santander.challenge.ms_banks.adapter.output.persistence.entity.BankEntity;
import com.santander.challenge.ms_banks.adapter.output.persistence.repository.BankJpaRepository;
import com.santander.challenge.ms_banks.application.mapper.BankMapper;
import com.santander.challenge.ms_banks.domain.model.Bank;
import com.santander.challenge.ms_banks.domain.port.output.BankRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter that implements BankRepositoryPort using JPA, bridging persistence with the domain layer.
 */
@Component
public class BankRepositoryAdapter implements BankRepositoryPort {
    
    private final BankJpaRepository jpaRepository;
    private final BankMapper bankMapper;
    
    public BankRepositoryAdapter(BankJpaRepository jpaRepository, BankMapper bankMapper) {
        this.jpaRepository = jpaRepository;
        this.bankMapper = bankMapper;
    }
    
    @Override
    public Bank save(Bank bank) {
        BankEntity entity = bankMapper.toEntity(bank);
        BankEntity savedEntity = jpaRepository.save(entity);
        return bankMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Bank> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(bankMapper::toDomain);
    }
    
    @Override
    public Page<Bank> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(bankMapper::toDomain);
    }
    
    @Override
    public Page<Bank> findByCountry(String country, Pageable pageable) {
        return jpaRepository.findByCountry(country, pageable)
                .map(bankMapper::toDomain);
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
    
    @Override
    public Optional<Bank> findByCode(String code) {
        return jpaRepository.findByCode(code)
                .map(bankMapper::toDomain);
    }
    
    @Override
    public boolean existsByCodeAndIdNot(String code, UUID excludeId) {
        return jpaRepository.existsByCodeAndIdNot(code, excludeId);
    }
}

