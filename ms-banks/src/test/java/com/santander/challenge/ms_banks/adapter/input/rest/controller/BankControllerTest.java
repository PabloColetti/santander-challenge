package com.santander.challenge.ms_banks.adapter.input.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.challenge.ms_banks.adapter.input.rest.dto.request.CreateBankRequest;
import com.santander.challenge.ms_banks.adapter.input.rest.dto.response.BankResponse;
import com.santander.challenge.ms_banks.application.mapper.BankMapper;
import com.santander.challenge.ms_banks.application.service.BankInternalService;
import com.santander.challenge.ms_banks.domain.model.Bank;
import com.santander.challenge.ms_banks.domain.port.input.BankServicePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankController.class)
class BankControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private BankServicePort bankService;
    
    @MockBean
    private BankInternalService bankInternalService;
    
    @MockBean
    private BankMapper bankMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static final UUID TEST_BANK_ID = UUID.randomUUID();
    
    @Test
    void createBank_Success() throws Exception {
        CreateBankRequest request = new CreateBankRequest();
        request.setCode("BANK001");
        request.setName("Test Bank");
        request.setCountry("Argentina");
        request.setEmail("test@bank.com");
        
        Bank bank = Bank.builder()
                .id(TEST_BANK_ID)
                .code("BANK001")
                .name("Test Bank")
                .build();
        
        BankResponse response = BankResponse.builder()
                .id(TEST_BANK_ID)
                .code("BANK001")
                .name("Test Bank")
                .build();
        
        when(bankMapper.toDomain(any(CreateBankRequest.class))).thenReturn(bank);
        when(bankService.createBank(any(Bank.class))).thenReturn(bank);
        when(bankMapper.toResponse(any(Bank.class))).thenReturn(response);
        
        mockMvc.perform(post("/api/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_BANK_ID.toString()))
                .andExpect(jsonPath("$.code").value("BANK001"));
    }
    
    @Test
    void getBankById_Success() throws Exception {
        Bank bank = Bank.builder()
                .id(TEST_BANK_ID)
                .code("BANK001")
                .name("Test Bank")
                .build();
        
        BankResponse response = BankResponse.builder()
                .id(TEST_BANK_ID)
                .code("BANK001")
                .name("Test Bank")
                .build();
        
        when(bankService.getBankById(TEST_BANK_ID)).thenReturn(bank);
        when(bankMapper.toResponse(any(Bank.class))).thenReturn(response);
        
        mockMvc.perform(get("/api/banks/" + TEST_BANK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_BANK_ID.toString()))
                .andExpect(jsonPath("$.code").value("BANK001"));
    }
    
    @Test
    void getAllBanks_Success() throws Exception {
        Bank bank = Bank.builder()
                .id(TEST_BANK_ID)
                .code("BANK001")
                .build();
        
        BankResponse response = BankResponse.builder()
                .id(TEST_BANK_ID)
                .code("BANK001")
                .build();
        
        Page<Bank> bankPage = new PageImpl<>(Arrays.asList(bank));
        Page<BankResponse> responsePage = new PageImpl<>(Arrays.asList(response));
        
        when(bankService.getAllBanks(any())).thenReturn(bankPage);
        when(bankMapper.toResponse(any(Bank.class))).thenReturn(response);
        
        mockMvc.perform(get("/api/banks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(TEST_BANK_ID.toString()));
    }
    
    @Test
    void deleteBank_Success() throws Exception {
        doNothing().when(bankService).deleteBank(TEST_BANK_ID);
        mockMvc.perform(delete("/api/banks/" + TEST_BANK_ID))
                .andExpect(status().isNoContent());
    }
}
