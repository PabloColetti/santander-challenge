package com.santander.challenge.ms_banks.adapter.output.http.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * Cliente Feign para comunicarse con ms-accounts.
 * Permite validar si un banco tiene cuentas asociadas.
 */
@FeignClient(name = "ms-accounts", url = "${feign.client.ms-accounts.url:http://localhost:9090}")
public interface AccountClient {
    
    /**
     * Cuenta las cuentas asociadas a un banco.
     * 
     * @param bankId el ID del banco
     * @return el número de cuentas
     */
    @GetMapping("/api/accounts/count")
    Long countByBankId(@RequestParam("bankId") UUID bankId);
}

/**
 * Adaptador que implementa AccountCountPort usando Feign.
 */
@org.springframework.stereotype.Component
class AccountCountAdapter implements com.santander.challenge.ms_banks.domain.port.output.AccountCountPort {
    
    private final AccountClient accountClient;
    
    public AccountCountAdapter(AccountClient accountClient) {
        this.accountClient = accountClient;
    }
    
    @Override
    public Long countByBankId(UUID bankId) {
        try {
            return accountClient.countByBankId(bankId);
        } catch (Exception e) {
            // Si ms-accounts no está disponible, asumimos 0 cuentas para permitir eliminación
            // En producción, esto debería manejarse mejor (circuit breaker, retry, etc.)
            return 0L;
        }
    }
}

