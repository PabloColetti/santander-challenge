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
 * Controller REST para operaciones CRUD de bancos.
 */
@Tag(name = "Banks", description = "API para gestión de bancos")
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
    @Operation(
            summary = "Crear banco",
            description = "Crea un nuevo banco. El código del banco debe ser único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Banco creado exitosamente",
                    content = @Content(schema = @Schema(implementation = BankResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El código del banco ya existe",
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
     * Obtiene un banco por ID.
     */
    @Operation(
            summary = "Obtener banco por ID",
            description = "Obtiene la información de un banco específico mediante su UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Banco encontrado",
                    content = @Content(schema = @Schema(implementation = BankResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Banco no encontrado",
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
     * Lista todos los bancos con paginación.
     */
    @Operation(
            summary = "Listar bancos",
            description = "Lista todos los bancos con paginación. Opcionalmente puede filtrar por país."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de bancos obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = Page.class))
            )
    })
    @GetMapping
    public ResponseEntity<Page<BankResponse>> getAllBanks(
            @Parameter(description = "País para filtrar (opcional)", example = "España")
            @RequestParam(required = false) String country,
            @Parameter(description = "Parámetros de paginación (page, size, sort)")
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
    @Operation(
            summary = "Actualizar banco",
            description = "Actualiza la información de un banco existente. El código no puede modificarse."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Banco actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = BankResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Banco no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<BankResponse> updateBank(
            @Parameter(description = "UUID del banco", required = true)
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
    @Operation(
            summary = "Eliminar banco",
            description = "Elimina un banco. Solo se puede eliminar si no tiene cuentas asociadas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Banco eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Banco no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El banco tiene cuentas asociadas y no puede ser eliminado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(
            @Parameter(description = "UUID del banco", required = true)
            @PathVariable UUID id) {
        bankService.deleteBank(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Endpoint interno que consume el endpoint GET /api/banks/{id} (llamada a sí mismo).
     * Demuestra la capacidad del microservicio de consumir sus propios endpoints.
     */
    @Operation(
            summary = "Obtener banco (interno)",
            description = "Endpoint interno que consume el endpoint GET /api/banks/{id} mediante Feign. " +
                    "Demuestra la capacidad del microservicio de consumir sus propios endpoints."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Banco encontrado",
                    content = @Content(schema = @Schema(implementation = BankResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Banco no encontrado",
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

