package com.fiap.mspagamento.valueobjects;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Pagamento {

    private UUID id;
    private UUID pedidoId;
    private String numeroCartao;
    private BigDecimal valor;
    private StatusPagamento status;
    private LocalDateTime criadoEm;

    public Pagamento(UUID id, UUID pedidoId, String numeroCartao, BigDecimal valor, StatusPagamento status, LocalDateTime criadoEm) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.numeroCartao = numeroCartao;
        this.valor = valor;
        this.status = status;
        this.criadoEm = criadoEm;
    }

    public UUID getId() { return id; }
    public UUID getPedidoId() { return pedidoId; }
    public String getNumeroCartao() { return numeroCartao; }
    public BigDecimal getValor() { return valor; }
    public StatusPagamento getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }
}
