package com.santander.challenge.ms_banks.adapter.input.rest.exception;

/**
 * Códigos de error del microservicio ms-banks.
 * Cada código identifica de manera única un tipo de error.
 */
public enum ErrorCode {
    
    // Errores de recurso no encontrado (4xx)
    BANK_NOT_FOUND("BANK_NOT_FOUND", "Bank not found"),
    
    // Errores de validación (4xx)
    BANK_VALIDATION_ERROR("BANK_VALIDATION_ERROR", "Bank validation error"),
    INVALID_REQUEST("INVALID_REQUEST", "Invalid request data"),
    
    // Errores de conflicto (4xx)
    DUPLICATE_BANK_CODE("DUPLICATE_BANK_CODE", "Bank code already exists"),
    BANK_HAS_ACCOUNTS("BANK_HAS_ACCOUNTS", "Cannot delete bank with associated accounts"),
    
    // Errores del servidor (5xx)
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error"),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "Service temporarily unavailable");
    
    private final String code;
    private final String description;
    
    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}

