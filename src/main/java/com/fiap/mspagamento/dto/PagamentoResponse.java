package com.fiap.mspagamento.dto;

import com.fiap.mspagamento.valueobjects.Pagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagamentoResponse(
        UUID id,
        UUID pedidoId,
        BigDecimal valor,
        String status,
        LocalDateTime criadoEm
) {
    public static PagamentoResponse fromEntity(Pagamento pagamento) {
        return new PagamentoResponse(
                pagamento.getId(),
                pagamento.getPedidoId(),
                pagamento.getValor(),
                pagamento.getStatus().name(),
                pagamento.getCriadoEm()
        );
    }
}
