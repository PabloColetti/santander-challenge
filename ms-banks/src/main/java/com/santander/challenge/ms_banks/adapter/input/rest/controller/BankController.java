package com.santander.challenge.ms_banks.adapter.input.rest.controller;

import com.santander.challenge.ms_banks.adapter.input.rest.dto.request.CreateBankRequest;
import com.santander.challenge.ms_banks.adapter.input.rest.dto.request.UpdateBankRequest;
import com.santander.challenge.ms_banks.adapter.input.rest.dto.response.BankResponse;
import com.santander.challenge.ms_banks.application.mapper.BankMapper;
import com.santander.challenge.ms_banks.application.service.BankInternalService;
import com.santander.challenge.ms_banks.domain.port.input.BankServicePort;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller REST para operaciones CRUD de bancos.
 */
@RestController
@RequestMapping("/api/banks")
public class BankController {
    
    private final BankServicePort bankService;
    private final BankInternalService bankInternalService;
    private final BankMapper bankMapper;
    
    public BankController(BankServicePort bankService, 
                         BankInternalService bankInternalService,
                         BankMapper bankMapper) {
        this.bankService = bankService;
        this.bankInternalService = bankInternalService;
        this.bankMapper = bankMapper;
    }
    
    /**
     * Crea un nuevo banco.
     */
    @PostMapping
    public ResponseEntity<BankResponse> createBank(@Valid @RequestBody CreateBankRequest request) {
        var bank = bankMapper.toDomain(request);
        var createdBank = bankService.createBank(bank);
        var response = bankMapper.toResponse(createdBank);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Obtiene un banco por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BankResponse> getBankById(@PathVariable UUID id) {
        var bank = bankService.getBankById(id);
        var response = bankMapper.toResponse(bank);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lista todos los bancos con paginación.
     */
    @GetMapping
    public ResponseEntity<Page<BankResponse>> getAllBanks(
            @RequestParam(required = false) String country,
            @PageableDefault(size = 10) Pageable pageable) {
        
        Page<BankResponse> response;
        if (country != null && !country.isEmpty()) {
            response = bankService.getBanksByCountry(country, pageable)
                    .map(bankMapper::toResponse);
        } else {
            response = bankService.getAllBanks(pageable)
                    .map(bankMapper::toResponse);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Actualiza un banco existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BankResponse> updateBank(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBankRequest request) {
        var bank = bankMapper.toDomain(request);
        var updatedBank = bankService.updateBank(id, bank);
        var response = bankMapper.toResponse(updatedBank);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Elimina un banco.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable UUID id) {
        bankService.deleteBank(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Endpoint interno que consume el endpoint GET /api/banks/{id} (llamada a sí mismo).
     * Demuestra la capacidad del microservicio de consumir sus propios endpoints.
     */
    @GetMapping("/{id}/internal")
    public ResponseEntity<BankResponse> getBankByIdInternal(@PathVariable UUID id) {
        var bank = bankInternalService.getBankByIdInternal(id);
        var response = bankMapper.toResponse(bank);
        return ResponseEntity.ok(response);
    }
}

