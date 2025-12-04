package com.santander.challenge.ms_banks.adapter.input.rest.controller;

import com.santander.challenge.ms_banks.adapter.input.rest.dto.request.CreateBankRequest;
import com.santander.challenge.ms_banks.adapter.input.rest.dto.request.UpdateBankRequest;
import com.santander.challenge.ms_banks.adapter.input.rest.dto.response.BankResponse;
import com.santander.challenge.ms_banks.application.mapper.BankMapper;
import com.santander.challenge.ms_banks.application.service.BankInternalService;
import com.santander.challenge.ms_banks.domain.port.input.BankServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller that exposes CRUD operations for banks.
 */
@Tag(name = "Banks", description = "API for bank management")
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
     * Creates a new bank.
     */
    @Operation(
            summary = "Create bank",
            description = "Creates a new bank. The bank code must be unique."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Bank created successfully",
                    content = @Content(schema = @Schema(implementation = BankResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "The bank code already exists",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<BankResponse> createBank(@Valid @RequestBody CreateBankRequest request) {
        var bank = bankMapper.toDomain(request);
        var createdBank = bankService.createBank(bank);
        var response = bankMapper.toResponse(createdBank);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Retrieves a bank by its identifier.
     */
    @Operation(
            summary = "Get bank by ID",
            description = "Retrieves the information of a specific bank by UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Bank found",
                    content = @Content(schema = @Schema(implementation = BankResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bank not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<BankResponse> getBankById(
            @Parameter(description = "UUID del banco", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        var bank = bankService.getBankById(id);
        var response = bankMapper.toResponse(bank);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lists banks with optional pagination and country filtering.
     */
    @Operation(
            summary = "List banks",
            description = "Lists all banks with pagination and an optional country filter."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Bank list retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class))
            )
    })
    @GetMapping
    public ResponseEntity<Page<BankResponse>> getAllBanks(
            @Parameter(description = "Optional country filter", example = "Spain")
            @RequestParam(required = false) String country,
            @Parameter(description = "Pagination parameters (page, size, sort)")
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
     * Updates an existing bank.
     */
    @Operation(
            summary = "Update bank",
            description = "Updates the information of an existing bank. The code cannot be modified."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Bank updated successfully",
                    content = @Content(schema = @Schema(implementation = BankResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bank not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<BankResponse> updateBank(
            @Parameter(description = "Bank UUID", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBankRequest request) {
        var bank = bankMapper.toDomain(request);
        var updatedBank = bankService.updateBank(id, bank);
        var response = bankMapper.toResponse(updatedBank);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Deletes a bank when it has no associated accounts.
     */
    @Operation(
            summary = "Delete bank",
            description = "Deletes a bank only if there are no associated accounts."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Bank deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bank not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "The bank has associated accounts and cannot be deleted",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(
            @Parameter(description = "Bank UUID", required = true)
            @PathVariable UUID id) {
        bankService.deleteBank(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Internal endpoint that self-consumes GET /api/banks/{id} through Feign to demonstrate internal calls.
     */
    @Operation(
            summary = "Get bank (internal)",
            description = "Internal endpoint that consumes GET /api/banks/{id} through Feign to demonstrate internal calls."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Bank found",
                    content = @Content(schema = @Schema(implementation = BankResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bank not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}/internal")
    public ResponseEntity<BankResponse> getBankByIdInternal(
            @Parameter(description = "UUID del banco", required = true)
            @PathVariable UUID id) {
        var bank = bankInternalService.getBankByIdInternal(id);
        var response = bankMapper.toResponse(bank);
        return ResponseEntity.ok(response);
    }
}

