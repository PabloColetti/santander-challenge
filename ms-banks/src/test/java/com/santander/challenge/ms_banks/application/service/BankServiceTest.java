package com.santander.challenge.ms_banks.application.service;

import com.santander.challenge.ms_banks.domain.exception.BankNotFoundException;
import com.santander.challenge.ms_banks.domain.exception.DuplicateBankException;
import com.santander.challenge.ms_banks.domain.model.Bank;
import com.santander.challenge.ms_banks.domain.port.output.AccountCountPort;
import com.santander.challenge.ms_banks.domain.port.output.BankRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {
    
    @Mock
    private BankRepositoryPort bankRepository;
    
    @Mock
    private AccountCountPort accountCountPort;
    
    @InjectMocks
    private BankService bankService;
    
    private Bank testBank;
    private UUID testBankId;
    
    @BeforeEach
    void setUp() {
        testBankId = UUID.randomUUID();
        testBank = Bank.builder()
                .id(testBankId)
                .code("BANK001")
                .name("Test Bank")
                .country("Argentina")
                .address("Test Address")
                .phone("1234567890")
                .email("test@bank.com")
                .build();
    }
    
    @Test
    void createBank_Success() {
        // Given
        when(bankRepository.existsByCode("BANK001")).thenReturn(false);
        when(bankRepository.save(any(Bank.class))).thenAnswer(invocation -> {
            Bank bank = invocation.getArgument(0);
            bank.setId(testBankId);
            bank.setCreatedAt(LocalDateTime.now());
            bank.setUpdatedAt(LocalDateTime.now());
            return bank;
        });
        
        // When
        Bank result = bankService.createBank(testBank);
        
        // Then
        assertNotNull(result);
        assertEquals("BANK001", result.getCode());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(bankRepository).existsByCode("BANK001");
        verify(bankRepository).save(any(Bank.class));
    }
    
    @Test
    void createBank_DuplicateCode_ThrowsException() {
        // Given
        when(bankRepository.existsByCode("BANK001")).thenReturn(true);
        
        // When & Then
        assertThrows(DuplicateBankException.class, () -> bankService.createBank(testBank));
        verify(bankRepository).existsByCode("BANK001");
        verify(bankRepository, never()).save(any(Bank.class));
    }
    
    @Test
    void getBankById_Success() {
        // Given
        when(bankRepository.findById(testBankId)).thenReturn(Optional.of(testBank));
        
        // When
        Bank result = bankService.getBankById(testBankId);
        
        // Then
        assertNotNull(result);
        assertEquals(testBankId, result.getId());
        verify(bankRepository).findById(testBankId);
    }
    
    @Test
    void getBankById_NotFound_ThrowsException() {
        // Given
        when(bankRepository.findById(testBankId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(BankNotFoundException.class, () -> bankService.getBankById(testBankId));
        verify(bankRepository).findById(testBankId);
    }
    
    @Test
    void getAllBanks_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Bank> bankPage = new PageImpl<>(Arrays.asList(testBank));
        when(bankRepository.findAll(pageable)).thenReturn(bankPage);
        
        // When
        Page<Bank> result = bankService.getAllBanks(pageable);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(bankRepository).findAll(pageable);
    }
    
    @Test
    void updateBank_Success() {
        // Given
        Bank updatedBank = Bank.builder()
                .code("BANK002")
                .name("Updated Bank")
                .country("Brazil")
                .build();
        
        when(bankRepository.findById(testBankId)).thenReturn(Optional.of(testBank));
        when(bankRepository.existsByCodeAndIdNot("BANK002", testBankId)).thenReturn(false);
        when(bankRepository.save(any(Bank.class))).thenAnswer(invocation -> {
            Bank bank = invocation.getArgument(0);
            bank.setUpdatedAt(LocalDateTime.now());
            return bank;
        });
        
        // When
        Bank result = bankService.updateBank(testBankId, updatedBank);
        
        // Then
        assertNotNull(result);
        verify(bankRepository).findById(testBankId);
        verify(bankRepository).existsByCodeAndIdNot("BANK002", testBankId);
        verify(bankRepository).save(any(Bank.class));
    }
    
    @Test
    void deleteBank_Success() {
        // Given
        when(bankRepository.findById(testBankId)).thenReturn(Optional.of(testBank));
        when(accountCountPort.countByBankId(testBankId)).thenReturn(0L);
        
        // When
        bankService.deleteBank(testBankId);
        
        // Then
        verify(bankRepository).findById(testBankId);
        verify(accountCountPort).countByBankId(testBankId);
        verify(bankRepository).deleteById(testBankId);
    }
    
    @Test
    void deleteBank_NotFound_ThrowsException() {
        // Given
        when(bankRepository.findById(testBankId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(BankNotFoundException.class, () -> bankService.deleteBank(testBankId));
        verify(bankRepository).findById(testBankId);
        verify(accountCountPort, never()).countByBankId(any());
        verify(bankRepository, never()).deleteById(any());
    }
}
