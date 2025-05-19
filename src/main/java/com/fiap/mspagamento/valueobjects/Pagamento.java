package com.fiap.mspagamento.valueobjects;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Pagamento {

    private UUID id;
    private UUID pedidoId;
    private String numeroCartao;
    private BigDecimal valorTotal;
    private StatusPagamento status;
    private LocalDateTime criadoEm;

    public Pagamento(UUID id, UUID pedidoId, String numeroCartao, BigDecimal valorTotal, StatusPagamento status, LocalDateTime criadoEm) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.numeroCartao = numeroCartao;
        this.valorTotal = valorTotal;
        this.status = status;
        this.criadoEm = criadoEm;
    }

    public UUID getId() { return id; }
    public UUID getPedidoId() { return pedidoId; }
    public String getNumeroCartao() { return numeroCartao; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public StatusPagamento getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }
}
