package com.fiap.mspagamento.dto;

import com.fiap.mspagamento.valueobjects.Pagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagamentoResponse(
        UUID id,
        UUID pedidoId,
        BigDecimal valorTotal,
        String status,
        LocalDateTime criadoEm
)

{

    public static PagamentoResponse fromEntity(Pagamento pagamento) {
        String statusPedido = switch (pagamento.getStatus()) {
            case SUCESSO -> "PROCESSADO_SUCESSO";
            case FALHA_CARTAO -> "PROCESSADO_SEM_CREDITO";
            default -> "PROCESSADO_ERRO";
        };

        return new PagamentoResponse(
                pagamento.getId(),
                pagamento.getPedidoId(),
                pagamento.getValorTotal(),
                statusPedido,
                pagamento.getCriadoEm()
        );
    }

}
